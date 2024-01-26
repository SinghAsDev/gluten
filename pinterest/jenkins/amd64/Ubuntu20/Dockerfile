FROM 998131032990.dkr.ecr.us-east-1.amazonaws.com/java-8-amazon-corretto-jdk-ubuntu20.04:latest AS build_stage
SHELL ["/bin/bash", "-c"]

ENV NUM_THREADS 32
ENV IS_JENKINS_BUILD "true"

WORKDIR /gluten
COPY . /gluten

RUN apt-get update && apt-get install -y sudo python3
RUN ./dev/vcpkg/setup-build-depends.sh

RUN ./dev/buildbundle-veloxbe.sh

FROM scratch AS export-stage
COPY --from=build_stage gluten/package/target .
