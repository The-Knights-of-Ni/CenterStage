FROM rust:1 AS build

# Install basic opencv and rust dependencies
RUN --mount=target=/var/lib/apt/lists,type=cache,sharing=locked \
    --mount=target=/var/cache/apt,type=cache,sharing=locked \
    rm -f /etc/apt/apt.conf.d/docker-clean \
    && apt-get update \
    && apt-get -y --no-install-recommends install \
       libopencv-dev clang libclang-dev ninja-build

# Install build dependencies
RUN --mount=target=/var/lib/apt/lists,type=cache,sharing=locked \
    --mount=target=/var/cache/apt,type=cache,sharing=locked \
    rm -f /etc/apt/apt.conf.d/docker-clean \
    && apt-get -y --no-install-recommends install \
       wget unzip


# Install java \
RUN --mount=target=/var/lib/apt/lists,type=cache,sharing=locked \
    --mount=target=/var/cache/apt,type=cache,sharing=locked \
    rm -f /etc/apt/apt.conf.d/docker-clean \
    && apt-get -y --no-install-recommends install \
       default-jdk

# Install cross compilation toolchain
RUN rustup target add armv7-linux-androideabi

# Install Android SDK+NDK
## Download
ADD "https://dl.google.com/android/repository/commandlinetools-linux-6200805_latest.zip" /tmp

## Extract
RUN mkdir /opt/android
RUN mkdir /opt/android/cmdline-tools
RUN unzip -d /opt/android/cmdline-tools /tmp/commandlinetools-linux-6200805_latest.zip

## Install
RUN yes Y | /opt/android/cmdline-tools/tools/bin/sdkmanager --install "build-tools;33.0.3" "platform-tools" "ndk;26.0.10792818" "platforms;android-33"

## License
RUN yes Y | /opt/android/cmdline-tools/tools/bin/sdkmanager --licenses

# Install Gradle
## Download
ADD "https://services.gradle.org/distributions/gradle-8.4-bin.zip" /tmp

## Extract
RUN mkdir /opt/gradle
RUN unzip -d /opt/gradle /tmp/gradle-8.4-bin.zip


# Clean up
RUN rm -rf /tmp/*

# Image Data
LABEL version="0.2.0"
LABEL description="Base image for building for android."
LABEL maintainer="Ashwin Naren"
