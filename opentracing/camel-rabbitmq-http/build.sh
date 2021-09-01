#https://github.com/opentracing-contrib/nginx-opentracing
IMAGE_NAME=emmerson/camel-rabbitmq-http:1.0.0-params

#IMAGE_ID=$(docker build -t $IMAGE_NAME . | grep "Successfully built " | awk '{print $3}')
docker build -t $IMAGE_NAME . --iidfile imageid.txt
IMAGE_ID=$(cat imageid.txt)

echo "$IMAGE_NAME Image ID: $IMAGE_ID"