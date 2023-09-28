#[allow(non_snake_case)]
use jni::objects::JClass;
use jni::sys::jstring;
use jni::JNIEnv;

// The native function implemented in Rust.
#[no_mangle] // Keep the function name the way it is
pub unsafe extern "C" fn Java_org_theknightsofni_pidrs_PID_nativeRun( // Java_[package]_[class]_[method]
    env: JNIEnv,
    _: JClass,
) -> jstring {
    // TODO: do something useful
    return jstring::from(env, "Hello from Rust!");
}
