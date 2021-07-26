FROM opentracing/nginx-opentracing

COPY ./etc/ /etc/
RUN apt update -y && apt upgrade -y && apt install curl -y


EXPOSE 80
EXPOSE 443

CMD ["nginx", "-g", "daemon off;"]