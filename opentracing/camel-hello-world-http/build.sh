#https://github.com/opentracing-contrib/nginx-opentracing
IMAGE_NAME=emmerson/camel-hello-world-http:2.0.0

#IMAGE_ID=$(docker build -t $IMAGE_NAME . | grep "Successfully built " | awk '{print $3}')
docker build -t $IMAGE_NAME . --iidfile imageid.txt
IMAGE_ID=$(cat imageid.txt)

echo "Image ID: $IMAGE_ID"