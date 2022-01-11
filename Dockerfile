FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.11_9_openj9-0.26.0-alpine-slim
WORKDIR /home/app
COPY layers/libs /home/app/libs
COPY layers/resources /home/app/resources
COPY layers/application.jar /home/app/application.jar
EXPOSE 8080 80 443 8443
ENTRYPOINT ["java", "-jar", "/home/app/application.jar"]
