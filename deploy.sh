#!/bin/zsh

set -e

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
echo "Testing version $VERSION"
mvn clean test
echo "Building docker image for version $VERSION"
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=192.168.4.30:5000/englishdaily:${VERSION} -DskipTests
echo "Pushing to docker registry"
docker push 192.168.4.30:5000/englishdaily:${VERSION}
echo "Deploying to kubernetes"
kubectl -n projects set image deployments.apps englishdaily englishdaily=192.168.4.30:5000/englishdaily:${VERSION}
echo "Inspecting deployment"
kubectl -n projects get po -w