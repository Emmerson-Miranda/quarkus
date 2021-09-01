FROM registry.access.redhat.com/ubi8/ubi-minimal:8.4
#https://quarkus.io/guides/building-native-image
#https://developers.redhat.com/blog/2019/05/31/working-with-red-hat-enterprise-linux-universal-base-images-ubi#a_sample_application_using_ubi


WORKDIR /work/

COPY ./camel-rabbitmq-http-1.0.0-runner /work/application
COPY ./startup.sh /work/startup.sh
#RUN apt update -y && apt upgrade -y && apt install curl -y

RUN microdnf update && microdnf install procps
# set up permissions for user `1001`
RUN chmod 775 /work /work/application /work/startup.sh \
  && chown -R 1001 /work \
  && chmod -R "g+rwX" /work \
  && chown -R 1001:root /work

EXPOSE 8080
USER 1001

CMD ["./startup.sh"]