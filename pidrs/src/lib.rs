#[allow(non_snake_case)]
use jni::objects::JClass;
use jni::sys::jdouble;
use jni::JNIEnv;
use jni::objects::{JDoubleArray, JString};

// The native function implemented in Rust.
#[no_mangle] // Keep the function name the way it is
pub extern "system" fn Java_org_theknightsofni_pidrs_PID_nativeRun<'local>( // Java_[package]_[class]_[method]
    env: JNIEnv<'local>,
    _: JClass<'local>,
) -> JString<'local> {
    // TODO: do something useful
    let output = env
        .new_string("Hello, from rust!")
        .expect("Couldn't create java string!");
    output
}

#[no_mangle]
pub extern "system" fn Java_org_theknightsofni_pidrs_PID_nativeCalcMotorPowers<'local>(
    env: JNIEnv<'local>,
    _: JClass<'local>,
    leftStickX: jdouble,
    leftStickY: jdouble,
  rightStickX: jdouble
) -> JDoubleArray<'local> {
    let r = leftStickX.hypot(leftStickY);
    let robotAngle = leftStickY.atan2(leftStickX) - std::f64::consts::PI / 4.0;
    let lrPower = r * robotAngle.sin() + rightStickX;
    let lfPower = r * robotAngle.cos() + rightStickX;
    let rrPower = r * robotAngle.cos() - rightStickX;
    let rfPower = r * robotAngle.sin() - rightStickX;
    let motorPowers: [jdouble; 4] = [jdouble::from(lfPower), jdouble::from(rfPower), jdouble::from(lrPower), jdouble::from(rrPower)];
    let jniArray = env.new_double_array(4).unwrap();
    env.set_double_array_region(&jniArray, 0, &motorPowers).unwrap();
    return jniArray;
}
