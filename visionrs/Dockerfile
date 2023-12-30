FROM android-dev AS build

WORKDIR /usr/src/visionrs
COPY . .

RUN cargo build --release

# To host the build for downloading
FROM python:3.12-alpine

WORKDIR /usr/src/visionrs-build
COPY --from=build /usr/src/visionrs/target/ /usr/src/visionrs-build/

EXPOSE 8080:8080
CMD ["python3", "-m", "http.server", "8080"]
