#[allow(non_snake_case)]
use jni::objects::JClass;
use jni::sys::jstring;
use jni::JNIEnv;

// The native function implemented in Rust.
#[no_mangle]
pub unsafe extern "C" fn Java_org_theknightsofni_pidrs_PID_nativeRun(
    env: JNIEnv,
    _: JClass,
) -> jstring {
    todo!("Implement something useful.")
}
