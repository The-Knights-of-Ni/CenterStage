#![allow(non_snake_case)]
use jni::objects::{JClass, JIntArray, JObject};
use jni::sys::{jdouble, jint};
use jni::JNIEnv;
use jni::objects::JDoubleArray;


// The native function implemented in Rust.
#[no_mangle] // Keep the function name the way it is
pub extern "system" fn Java_org_theknightsofni_pidrs_PIDrs_nativeRun<'local>(env: JNIEnv<'local>, _: JClass<'local>,
                                                                             frontLeft: JObject, frontRight: JObject,
                                                                             rearLeft: JObject, rearRight: JObject) {

}

#[no_mangle]
pub extern "system" fn Java_org_theknightsofni_pidrs_PIDrs_nativeCalcMotorDistances<'local>(env: JNIEnv<'local>, _: JClass<'local>, x: jdouble, y: jdouble, turnAngle: jdouble,
                                                                                            COUNTS_CORRECTION_X: jdouble, COUNTS_CORRECTION_Y: jdouble, COUNTS_PER_MM: jdouble, COUNTS_PER_DEGREE: jdouble) -> JIntArray<'local> {
    let newX = (x as f64) * (COUNTS_CORRECTION_X as f64) * (COUNTS_PER_MM as f64);
    let newY = (y as f64) * (COUNTS_CORRECTION_Y as f64) * (COUNTS_PER_MM as f64);
    let distance = (newX.powi(2) + newY.powi(2)).sqrt() * (2.0_f64).sqrt();
    let angle = newY.atan2(newX) - std::f64::consts::PI / 4.0;
    let turnAngleCount = turnAngle as f64 * (COUNTS_PER_DEGREE as f64);
    let tickCount: [jint; 4] = [(distance * angle.cos() - turnAngleCount) as i64 as jint,
        (distance * angle.sin() + turnAngleCount) as i64 as jint,
        (distance * angle.sin() - turnAngleCount) as i64 as jint,
        (distance * angle.cos() + turnAngleCount) as i64 as jint];
    let jniArray = env.new_int_array(4).unwrap();
    env.set_int_array_region(&jniArray, 0, &tickCount).unwrap();
    return jniArray;
}

#[no_mangle]
pub extern "system" fn Java_org_theknightsofni_pidrs_PIDrs_nativeCalcMotorPowers<'local>(env: JNIEnv<'local>, _: JClass<'local>, leftStickX: jdouble, leftStickY: jdouble, rightStickX: jdouble) -> JDoubleArray<'local> {
    let r = leftStickX.hypot(leftStickY);
    let robotAngle = leftStickY.atan2(leftStickX) - std::f64::consts::PI / 4.0;
    let flPower = r * robotAngle.cos() + rightStickX;
    let frPower = r * robotAngle.sin() - rightStickX;
    let rlPower = r * robotAngle.sin() + rightStickX;
    let rrPower = r * robotAngle.cos() - rightStickX;
    let motorPowers: [jdouble; 4] = [jdouble::from(flPower), jdouble::from(frPower), jdouble::from(rlPower), jdouble::from(rrPower)];
    let jniArray = env.new_double_array(4).unwrap();
    env.set_double_array_region(&jniArray, 0, &motorPowers).unwrap();
    return jniArray;
}

#[no_mangle]
pub extern "system" fn Java_org_theknightsofni_pidrs_PIDrs_nativeClamp<'local>(_env: JNIEnv<'local>, _: JClass<'local>, val: jdouble, min: jdouble, max: jdouble) -> jdouble {
    let output;
    if val >= min && val <= max {
        output = val
    } else if val < min {
        output = min
    } else {
        output = max
    }
    return output;
}
