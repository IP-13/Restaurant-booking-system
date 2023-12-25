FROM amazoncorretto:17-alpine-jdk
RUN echo ${PWD} && ls -l
COPY build/libs/user-service-1.0.0.jar user-service-1.0.0.jar
RUN echo ${PWD} && ls -l
ENTRYPOINT ["java", "-jar","/user-service-1.0.0.jar"]
