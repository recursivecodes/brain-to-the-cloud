# Brain-to-the-Cloud

This project represents the server portion of the Brain-to-the-Cloud project. It is a Micronaut application that listens to the MQTT queue that the client portion publishes brain data to and persists that brain data to an Autonomous DB instance in the cloud. It also contains a scheduled task to consume a serverless function that retrieves Call of Duty API data. This application combines the brain data and the video game stats to analyze the impact of brain focus and attention on the game performance.

## Learn More About this Project!

[Brain to the Cloud - Part I](https://recursive.codes/blog/post/2110)

[Brain to the Cloud - Part II](https://recursive.codes/blog/post/2108)

[Brain to the Cloud - Part III](https://recursive.codes/blog/post/2106)

## Saving Token

To run the import, you must retrieve your oauth token from my.callofduty.com and set it into the application.

To set the token into the application:

```shell
$ curl -i -X POST -H "Content-Type: application/json" -d '{"token": "OC...AA"}' --cookie "SESSION=Nz..001" http://localhost:8080/api/token
HTTP/1.1 200 OK
date: Mon, 7 Feb 2022 19:36:21 GMT
set-cookie: SESSION=Nz..001; Path=/; HTTPOnly
Authorization-Info: 78c4c893-0006-4857-910e-34261f78fd07
connection: keep-alive
transfer-encoding: chunked
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

```shell
$ docker build -t iad.ocir.io/idatzojkinhi/bttc/bttc-client:latest .
$ docker push iad.ocir.io/idatzojkinhi/bttc/bttc-client:latest
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
