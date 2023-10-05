use anyhow::Result;
use jni;
use jni::objects::{JClass, JString};
use jni::JNIEnv;
use opencv::prelude::*;

enum MarkerLocation {
    Left,
    Middle,
    Right,
    Unknown
}

fn getMarkerLocation() -> MarkerLocation {
    return MarkerLocation::Unknown; // TODO: Implement
}

#[no_mangle]
pub extern "system" fn Java_org_knightsofni_visionrs_NativeVision_process<'local>(
    // Notice that this `env` argument is mutable. Any `JNIEnv` API that may
    // allocate new object references will take a mutable reference to the
    // environment.
    mut env: JNIEnv<'local>,
    // this is the class that owns our static method. Not going to be used, but
    // still needs to have an argument slot
    _class: JClass<'local>,
    input: JString<'local>,
) -> JString<'local> {
    // First, we have to get the string out of java. Check out the `strings`
    // module for more info on how this works.
    let input: String = env
        .get_string(&input)
        .expect("Couldn't get java string!")
        .into();

    // Then we have to create a new java string to return. Again, more info
    // in the `strings` module.
    let output = env
        .new_string("test")
        .expect("Couldn't create java string!");
    output
}