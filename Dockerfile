FROM rust:1 AS build

# install basic opencv and rust dependencies
RUN --mount=target=/var/lib/apt/lists,type=cache,sharing=locked \
    --mount=target=/var/cache/apt,type=cache,sharing=locked \
    rm -f /etc/apt/apt.conf.d/docker-clean \
    && apt-get update \
    && apt-get -y --no-install-recommends install \
       libopencv-dev clang libclang-dev

# install java \
RUN --mount=target=/var/lib/apt/lists,type=cache,sharing=locked \
    --mount=target=/var/cache/apt,type=cache,sharing=locked \
    rm -f /etc/apt/apt.conf.d/docker-clean \
    && apt-get update \
    && apt-get -y --no-install-recommends install \
       default-jdk

# install android sdk
RUN --mount=target=/var/lib/apt/lists,type=cache,sharing=locked \
    --mount=target=/var/cache/apt,type=cache,sharing=locked \
    rm -f /etc/apt/apt.conf.d/docker-clean \
    && apt-get update \
    && apt-get -y --no-install-recommends install \
       android-sdk

# install cross compilation toolchain
# RUN rustup target add armv7-linux-androideabi

WORKDIR /usr/src/CenterStage
COPY . .

RUN ./gradlew assembleDebug

# To host the build for downloading
FROM python:3.12-alpine

WORKDIR /usr/src/server
# Make sure dir mactches the --release flag
COPY --from=build /usr/src/CenterStage /usr/src/server/

EXPOSE 8080:8080
CMD ["python3", "-m", "http.server", "8080"]
