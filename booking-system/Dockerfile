#FROM openjdk:17-jdk-slim-buster
#
#RUN apt-get update -y
#RUN apt-get install -y binutils
#
#WORKDIR /app
#
#COPY . .
#
#RUN ./gradlew build -x test -i --stacktrace
#
#ENTRYPOINT ["java", "-jar", "/app/build/libs/main-1.0.0.jar"]

FROM amazoncorretto:17-alpine-jdk
COPY build/libs/booking-system-1.0.0.jar booking-system-1.0.0.jar
ENTRYPOINT ["java", "-jar","/booking-system-1.0.0.jar"]