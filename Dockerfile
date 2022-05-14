FROM arm64v8/openjdk:11-slim-buster
WORKDIR /home/app
COPY build/libs/bttc-0.1-all.jar /home/app/application.jar
EXPOSE 8080 80 443 8443
ENTRYPOINT ["java", "-jar", "/home/app/application.jar"]
