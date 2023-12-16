import numpy as np
import matplotlib.pyplot as plt

vectorize_tail = lambda f: np.vectorize(f, excluded={0})


def accel_traj_gen(x0, x1, vmax, amax):
    dx = x1 - x0
    if vmax / amax < dx / vmax:
        # normal trajectory
        dt1 = vmax / amax
        dt2 = dx / vmax - vmax / amax
        dt3 = dt1
        return x0, (
            (amax, dt1),
            (0, dt2),
            (-amax, dt3)
        )
    else:
        # degenerate trajectory
        dt1 = np.sqrt(dx / amax)
        dt2 = dt1
        return x0, (
            (amax, dt1),
            (-amax, dt2)
        )


@vectorize_tail
def accel_traj_get_accel(traj, t):
    _, phases = traj
    for a, dt in phases:
        if t < dt:
            return a

        t -= dt
    return phases[-1][0]


@vectorize_tail
def accel_traj_get_vel(traj, t):
    _, phases = traj
    v0 = 0
    for a, dt in phases:
        if t < dt:
            return v0 + a * t

        v0 += a * dt

        t -= dt
    return v0


@vectorize_tail
def accel_traj_get_pos(traj, t):
    x0, phases = traj
    v0 = 0
    for a, dt in phases:
        if t < dt:
            return x0 + v0 * t + a * t ** 2 / 2

        x0 += v0 * dt + a * dt ** 2 / 2
        v0 += a * dt

        t -= dt
    return x0


def accel_traj_duration(traj):
    _, phases = traj
    duration = 0
    for _, dt in phases:
        duration += dt
    return duration


def spline_fit(x0, dx0, x1, dx1):
    a = 2 * x0 + dx0 - 2 * x1 + dx1
    b = -3 * x0 - 2 * dx0 + 3 * x1 - dx1
    c = dx0
    d = x0
    return a, b, c, d


def spline_get(spline, u):
    a, b, c, d = spline
    return a * u ** 3 + b * u ** 2 + c * u + d


def spline_deriv(spline, u):
    a, b, c, d = spline
    return 3 * a * u ** 2 + 2 * b * u + c


def spline_param_of_disp_deriv(x_spline, y_spline, u):
    return 1.0 / np.sqrt(spline_deriv(x_spline, u) ** 2 + spline_deriv(y_spline, u) ** 2)


x_spline = spline_fit(0, 36, 24, 30)
y_spline = spline_fit(0, -24, 24, -9)
u = np.linspace(0, 1, 100)

print(spline_get(x_spline, 0), spline_deriv(x_spline, 0), spline_get(x_spline, 1), spline_deriv(x_spline, 1))
print(spline_get(y_spline, 0), spline_deriv(y_spline, 0), spline_get(y_spline, 1), spline_deriv(y_spline, 1))

upsilon = np.linspace(0, 1, 100)
dupsilon = upsilon[1] - upsilon[0]
integrand = np.sqrt(
    spline_deriv(x_spline, upsilon) ** 2 +
    spline_deriv(y_spline, upsilon) ** 2
)

sums = np.zeros_like(upsilon)
last_sum = 0
for i in range(len(upsilon)):
    sums[i] = last_sum + integrand[i] * dupsilon
    last_sum = sums[i]


@np.vectorize
def spline_param_of_disp(s):
    for i in range(len(sums)):
        if s < sums[i]:
            return upsilon[i]


length = sums[-1]
traj = accel_traj_gen(0, length, 30, 30)
t = np.linspace(0, accel_traj_duration(traj), 100)

s = accel_traj_get_pos(traj, t)

u = spline_param_of_disp(s)

x = spline_get(x_spline, u)
y = spline_get(y_spline, u)

plt.title('Spline Trajectory Position')
plt.xlabel('time [s]')
plt.ylabel('position [in]')
plt.plot(t, x, label='x')
plt.plot(t, y, label='y')
plt.legend()
plt.show()

dsdt = accel_traj_get_vel(traj, t)

duds = spline_param_of_disp_deriv(x_spline, y_spline, u)

dxdu = spline_deriv(x_spline, u)
dydu = spline_deriv(y_spline, u)

dxdt = dxdu * duds * dsdt
dydt = dydu * duds * dsdt

plt.title('Spline Trajectory Velocity')
plt.xlabel('time [s]')
plt.ylabel('velocity [in/s]')
plt.plot(t, dxdt, label='x')
plt.plot(t, dydt, label='y')
plt.legend()
plt.show()
