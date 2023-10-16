[![Android CI](https://github.com/The-Knights-of-Ni/PowerPlay/actions/workflows/build.yml/badge.svg)](https://github.com/The-Knights-of-Ni/FreightFrenzy/actions/workflows/build.yml)
# CenterStage 2023-2024

## About


This repository contains FTC Team 5206's repository for the CENTERSTAGE (2023-2024) competition season.

## Branches

Create branches for individual components in the code. Do not commit directly to master. Do not commit branch merges on your own. Contact @albonec or @moogloof for any urgent merge requests.
# Docs
* [A GitHub Pages doc is there](https://the-knights-of-ni.github.io/5206-docs/)
* There is javadoc for most things.

## Rust compilation

- Install Rust: https://www.rust-lang.org/tools/install

Follow the step-by-step instructions for your OS.

### Windows

- Install choco by running this command: `Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))`
- Open an administrator Terminal/cmd/powershell and run `choco install llvm opencv`.
- Set the environment variables: `OPENCV_LINK_LIBS=opencv_world[opencvversionhere]` (opencv_world411 for example)
- Build with `cargo build --release` in the `visionrs` directory

### Mac

- Install brew if you haven't already with this command in the terminal: `/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"`
- Install llvm and opencv with this command in the terminal: `brew install llvm opencv`
- Build with `cargo build --release` in the `visionrs` directory

### Linux

- Install llvm and opencv with this command in the terminal: `sudo apt install llvm opencv` (APT based distros) or `sudo pacman -S llvm opencv` (arch) or `sudo dnf install llvm opencv` (fedora/rhel)
- Build with `cargo build --release` in the `visionrs` directory

### Troubleshooting

- Double-check clang and opencv are in your path
- Read this README: https://github.com/twistedfall/opencv-rust#rust-opencv-bindings

## opencv compilation

You must have ninja and cmake installed. You must also have the android ndk installed.

```shell
cmake -Hopencv -Bopencv-build -DANDROID_ABI=armeabi-v7a -DANDROID_PLATFORM=android-26 -DANDROID_NDK="C:\Users\ariha\AppData\Local\Android\Sdk\ndk\26.0.10792818\" -DCMAKE_TOOLCHAIN_FILE="C:\Users\ariha\AppData\Local\Android\Sdk\ndk\26.0.10792818\build\cmake\android.toolchain.cmake" -G Ninja
```

Then open up the cmake gui and configure.

```shell
cmake --build . --target install --config release
```
