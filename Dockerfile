FROM gradle:7.6.0-jdk17 AS build
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src
RUN chmod +x gradlew
RUN ./gradlew --no-daemon build
CMD ["./gradlew", "bootRun"]