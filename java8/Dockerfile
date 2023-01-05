FROM maven:3.6.3-jdk-8-slim AS build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:8u312-jre-slim
COPY --from=build /home/app/target/*.jar /usr/local/lib/scale-java8.jar

EXPOSE 7000

ENTRYPOINT ["java","-jar","/usr/local/lib/scale-java8.jar"]