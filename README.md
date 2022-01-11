# Brain-to-the-Cloud

This project represents the server portion of the Brain-to-the-Cloud project. It is a Micronaut application that listens to the MQTT queue that the client portion publishes brain data to and persists that brain data to an Autonomous DB instance in the cloud. It also contains a scheduled task to consume a serverless function that retrieves Call of Duty API data. This application combines the brain data and the video game stats to analyze the impact of brain focus and attention on the game performance.

## Saving Token

To run the serverless function, you must retrieve your oauth token from callofduty.com and set it into the application.

```shell
$ curl -i -X POST -H 'Content-Type: application/json' -d '{"token": "value"}' POST localhost:8080/token
```

## Docker

```shell
./gradlew dockerBuild
./gradlew dockerPush
```

```shell
$ docker build -t phx.ocir.io/toddrsharp/bttc/bttc-client:latest .
$ docker push phx.ocir.io/toddrsharp/bttc/bttc-client:latest
```

## SSL

Create key

```shell
$ mn create-key -k /projects/brain-to-the-cloud/certs -n bttc.toddrsharp.com-key.pem
```

Create ACME account

```shell
$ mn create-acme-account -k /projects/resources/certs -n acme-key.pem --lets-encrypt-prod -e you@email.com

Key creation complete. It can be found here /projects/resources/certs/acme-key.pem.
Account creation complete. Make sure to store your account pem somewhere safe as it is your only way to access your account.
```

## Todo

More documentation...