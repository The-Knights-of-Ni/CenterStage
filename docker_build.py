import shutil
import subprocess
import platform
import json
import pathlib
import os


def main():
    cwd = pathlib.Path(os.getcwd())
    if cwd.name != "CenterStage":
        print("Please run this script from the root of the repository")
        exit(1)
    docker = shutil.which("docker")
    if docker is None:
        print("Docker is not installed, please ensure it is installed and on your path")
        exit(1)
    if platform.system() == "Windows":
        docker += ".exe"
    print(f"Using docker at {docker}")
    print("Searching for android_dev image")
    out = subprocess.run([docker, "image", "ls", "--format", "json"], stdout=subprocess.PIPE)

    images = [json.loads(t) for t in out.stdout.decode().split("\n") if t != ""]
    android_dev = None
    for image in images:
        if image["Repository"] == "android-dev":
            android_dev = image
            break
    if android_dev is None:
        print("android_dev image not found, creating image (this will take a while)")
        subprocess.run([docker, "build", "-f", "docker/Dockerfile", "--tag", "android-dev", "."], cwd=str(cwd))
        print("android_dev image created, building project")
    else:
        print("android_dev image found, building project")
    print("checking android_dev image version")
    # TODO: check if the image is up to date
    subprocess.run([docker, "build", "--tag", "centerstage", "."], cwd=str(cwd))
    print("Image built, spawning container")
    subprocess.run([docker, "run", "-p", "8080:8080", "centerstage"], cwd=str(cwd))


if __name__ == "__main__":
    main()
