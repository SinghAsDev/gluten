FROM 998131032990.dkr.ecr.us-east-1.amazonaws.com/java-8-amazon-corretto-jdk-ubuntu20.04:latest AS build_stage
SHELL ["/bin/bash", "-c"]

RUN apt-get update && \
    apt-get -y install \
        sudo \
        locales \
        wget \
        tar \
        tzdata \
        git \
        ccache \
        cmake \
        ninja-build \
        build-essential \
        llvm-11-dev \
        clang-11 \
        libiberty-dev \
        libdwarf-dev \
        libre2-dev \
        libz-dev \
        libssl-dev \
        libboost-all-dev \
        libcurl4-openssl-dev \
        maven \
        curl \
        zip \
        unzip \
        pkg-config \
        bison \
        flex \
        autoconf-archive

ENV NUM_THREADS 32
ENV IS_JENKINS_BUILD "true"

WORKDIR /gluten
COPY . /gluten

RUN ./dev/buildbundle-veloxbe.sh

FROM scratch AS export-stage
COPY --from=build_stage gluten/package/target .
