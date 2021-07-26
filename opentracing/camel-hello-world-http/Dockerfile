FROM quay.io/quarkus/ubi-quarkus-native-image:21.1-java11

COPY ./camel-hello-world-http-2.0.0-SNAPSHOT-runner .
#RUN apt update -y && apt upgrade -y && apt install curl -y


EXPOSE 8080

ENTRYPOINT ["./camel-hello-world-http-2.0.0-SNAPSHOT-runner"]
