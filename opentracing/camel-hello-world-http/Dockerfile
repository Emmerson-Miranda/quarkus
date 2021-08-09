FROM registry.access.redhat.com/ubi8/ubi-minimal:8.4
#https://quarkus.io/guides/building-native-image

WORKDIR /work/

COPY ./camel-hello-world-http-2.0.0-SNAPSHOT-runner /work/application
#RUN apt update -y && apt upgrade -y && apt install curl -y

# set up permissions for user `1001`
RUN chmod 775 /work /work/application \
  && chown -R 1001 /work \
  && chmod -R "g+rwX" /work \
  && chown -R 1001:root /work

EXPOSE 8080
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]