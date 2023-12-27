FROM rust:1 AS build

RUN --mount=target=/var/lib/apt/lists,type=cache,sharing=locked \
    --mount=target=/var/cache/apt,type=cache,sharing=locked \
    rm -f /etc/apt/apt.conf.d/docker-clean \
    && apt-get update \
    && apt-get -y --no-install-recommends install \
       libopencv-dev clang libclang-dev

WORKDIR /usr/src/visionrs
COPY . .

RUN cargo build --release

# To host the build for downloading
FROM python:3.12-alpine

WORKDIR /usr/src/visionrs-build
# Make sure dir mactches the --release flag
COPY --from=build /usr/src/visionrs/target/release/ /usr/src/visionrs-build/

EXPOSE 8080:8080
CMD ["python3", "-m", "http.server", "8080"]
