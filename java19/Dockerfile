FROM gradle:jdk19 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:19-jdk

EXPOSE 7001

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/scale-java19.jar
RUN --from=build /app/*.jar /app/scale-java8.jar/

ENTRYPOINT ["java","--enable-preview","-jar","/app/scale-java19.jar"]