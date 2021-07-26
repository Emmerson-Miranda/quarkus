#https://github.com/opentracing-contrib/nginx-opentracing
IMAGE_NAME=emmerson/nginx:jaeger

#IMAGE_ID=$(docker build -t $IMAGE_NAME . | grep "Successfully built " | awk '{print $3}')
docker build -t $IMAGE_NAME . --iidfile imageid.txt
IMAGE_ID=$(cat imageid.txt)

echo "Image ID: $IMAGE_ID"