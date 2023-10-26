### multi-stage build
#FROM gradle:8.4.0-jdk17 AS BUILDER
#ENV HOME=/usr/app/
#WORKDIR $HOME
#COPY ./ $HOME
#RUN cd $HOME
#RUN #gradle clean
#RUN gradle build
#RUN ls -la $HOME/build/libs
#
## actual container, won't include any files from previous (BUILDER) stage
#FROM amazoncorretto:17-alpine-jdk
#USER root
#COPY --from=BUILDER $HOME/build/libs/app.jar .
#ENTRYPOINT exec java -jar app.jar

FROM amazoncorretto:17-alpine-jdk
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]