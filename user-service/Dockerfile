FROM gradle:8.2-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM amazoncorretto:17-alpine-jdk
RUN echo ${PWD} && ls -lR
COPY --from=build /home/gradle/src/build/libs/user-service-1.0.0.jar user-service-1.0.0.jar
ENTRYPOINT ["java", "-jar","/user-service-1.0.0.jar"]
