FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.11_9_openj9-0.26.0-alpine-slim
WORKDIR /home/app
COPY build/libs/bttc-0.1-all.jar /home/app/application.jar
EXPOSE 8080 80 443 8443
ENTRYPOINT ["java", "-jar", "/home/app/application.jar"]
