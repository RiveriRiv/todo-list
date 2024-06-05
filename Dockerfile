FROM gradle:7.6.0-jdk17 AS build
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME/
COPY gradle $APP_HOME/gradle
COPY src $APP_HOME/src
RUN ./gradlew --no-daemon build
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY src ./src
CMD ["./gradlew", "bootRun"]