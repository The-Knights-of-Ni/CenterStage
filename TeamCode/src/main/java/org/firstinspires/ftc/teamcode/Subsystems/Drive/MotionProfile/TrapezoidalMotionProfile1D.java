package org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile;

public class TrapezoidalMotionProfile1D implements MotionProfile1D {
    public double maxAcceleration;
    public double maxVelocity;
    public double distance;

    public TrapezoidalMotionProfile1D(double maxAcceleration, double maxVelocity, double distance) {
        this.maxAcceleration = maxAcceleration;
        this.maxVelocity = maxVelocity;
        this.distance = distance;
    }

    /**
     *  Return the current reference position based on the given motion profile times, maximum acceleration, velocity,
     *  and current time. Calculate the time it takes to accelerate to max velocity
     * @param time how much time has passed since the start of the motion profile, in milliseconds
     * @return Target position
     */
    @Override
    public double calculate(double time) {
        var acceleration_dt = maxVelocity / maxAcceleration;
        // If we can't accelerate to max velocity in the given distance, we'll accelerate as much as possible
        var halfway_distance = distance / 2;
        var acceleration_distance = 0.5 * maxAcceleration * Math.pow(acceleration_dt, 2.0);
        if (acceleration_distance > halfway_distance) {
            acceleration_dt = Math.sqrt(halfway_distance / (0.5 * maxAcceleration));
        }
        acceleration_distance = 0.5 * maxAcceleration * Math.pow(acceleration_dt, 2.0);

        // recalculate max velocity based on the time we have to accelerate and decelerate
        maxVelocity = maxAcceleration * acceleration_dt;
        // we decelerate at the same rate as we accelerate
        var deacceleration_dt = acceleration_dt;

        // calculate the time that we're at max velocity
        var cruise_distance = distance - 2 * acceleration_distance;
        var cruise_dt = cruise_distance / maxVelocity;
        var deacceleration_time = acceleration_dt + cruise_dt;

        // check if we're still in the motion profile
        var entire_dt = acceleration_dt + cruise_dt + deacceleration_dt;
        if (time > entire_dt) {
            return distance;
        }
        // if we're accelerating
        if (time < acceleration_dt) {
            // use the kinematic equation for acceleration
            return 0.5 * maxAcceleration * Math.pow(time, 2);
        }
        // if we're cruising
        else if (time < deacceleration_time) {
            acceleration_distance = 0.5 * maxAcceleration * Math.pow(acceleration_dt, 2);
            var cruise_current_dt = time - acceleration_dt;
            // use the kinematic equation for constant velocity
            return acceleration_distance + maxVelocity * cruise_current_dt;
        }
        // if we're decelerating
        else {
            acceleration_distance = 0.5 * maxAcceleration * Math.pow(acceleration_dt, 2);
            cruise_distance = maxVelocity * cruise_dt;
            deacceleration_time = time - deacceleration_time;
            // use the kinematic equations to calculate the instantaneous desired position
            return acceleration_distance + cruise_distance + maxVelocity * deacceleration_time - 0.5 * maxAcceleration * Math.pow(deacceleration_time, 2);
        }
    }
}
