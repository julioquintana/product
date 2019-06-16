#FROM registry.gitlab.com/cencosud-ds/post-venta/operacion-tienda/backend-ean-jda:build as build
FROM maven:alpine as build

RUN mkdir -p /app/src
#COPY nbactions.xml /app
COPY pom.xml /app

WORKDIR /app
RUN mvn dependency:go-offline



WORKDIR /app

COPY src /app/src
RUN mvn package

FROM openjdk:alpine
EXPOSE 7789

RUN mkdir -p /app
COPY --from=build /app/target/*.jar /app
WORKDIR /app
CMD java -jar *.jar