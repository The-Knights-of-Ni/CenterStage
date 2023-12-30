import os
import pathlib
import platform
import shutil
import subprocess

def main():
    cwd = pathlib.Path(os.getcwd())
    if cwd.name != "CenterStage":
        print("Please run this script from the root of the repository")
        exit(1)
    adb = shutil.which("adb")
    if adb is None:
        print("ADB is not installed, please ensure it is installed and on your path")
        exit(1)
    if platform.system() == "Windows":
        adb += ".exe"
    print(f"Using adb at {adb}")
    apk = cwd / "docker_out" / "outputs" / "apk" / "debug" / "app-debug.apk"
    if not apk.exists():
        print("apk not found, please build the project first, and copy the build to docker_out/")
        exit(1)
    print("Connecting to robot")
    result = subprocess.run([adb, "connect", "192.168.43.1:5555"])
    if result.returncode != 0:
        print("Failed to connect to robot, please ensure it is connected to the same network as your computer. Or adb might be broken.")
        exit(1)


if __name__ == "__main__":
    main()
