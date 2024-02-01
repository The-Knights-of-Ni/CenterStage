FROM android-dev AS build

# Enviorment
ARG GRADLE_HOME="/opt/gradle/gradle-8.4"
RUN export GRADLE_HOME="/opt/gradle/gradle-8.4"
RUN export PATH=${GRADLE_HOME}/bin:${PATH}

ARG ANDROID_HOME="/opt/android"
ARG ANDROID_NDK_HOME="/opt/android/ndk/21.1.6352462"
WORKDIR /usr/src/CenterStage
COPY . .

RUN /opt/gradle/gradle-8.4/bin/gradle assembleDebug

# To host the build for downloading
FROM python:3.12-alpine

WORKDIR /usr/src/server
COPY --from=build /usr/src/CenterStage /usr/src/server/src/
COPY --from=build /usr/src/CenterStage/TeamCode/build/ /usr/src/server/apk-build/
COPY --from=build /usr/src/CenterStage/visionrs/target/ /usr/src/server/visionrs-build/

EXPOSE 8080:8080
CMD ["python3", "-m", "http.server", "8080"]
