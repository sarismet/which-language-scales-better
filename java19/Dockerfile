FROM gradle:jdk19 AS gradleimage
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:19-jdk

RUN mkdir /app

COPY --from=gradleimage /home/gradle/src/build/libs/*-SNAPSHOT.jar /app/scale-java19.jar

EXPOSE 7001-7100

ENTRYPOINT ["java","--enable-preview","-jar","/app/scale-java19.jar"]
