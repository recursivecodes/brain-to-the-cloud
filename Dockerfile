FROM arm64v8/eclipse-temurin:11.0.15_10-jdk-focal
WORKDIR /home/app
COPY build/libs/bttc-0.1-all.jar /home/app/application.jar
EXPOSE 8080 80 443 8443
ENTRYPOINT ["java", "-jar", "/home/app/application.jar"]
