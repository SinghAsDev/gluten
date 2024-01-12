FROM 998131032990.dkr.ecr.us-east-1.amazonaws.com/java-11-amazon-corretto-jdk-ubuntu20.04:latest-arm64 AS build_stage
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

RUN curl -sSLO https://corretto.aws/downloads/latest/amazon-corretto-8-aarch64-linux-jdk.deb && dpkg -i amazon-corretto-8-aarch64-linux-jdk.deb
ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-amazon-corretto
ENV PATH /usr/lib/jvm/java-1.8.0-amazon-corretto/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

ENV CPU_TARGET "aarch64"
ENV NUM_THREADS 32
ENV VCPKG_FORCE_SYSTEM_BINARIES 1

WORKDIR /gluten
COPY . /gluten

# vcpkg does not compile on aarch64 currently
RUN ./dev/buildbundle-veloxbe.sh --enable_s3=ON --enable_vcpkg=OFF --build_type=Release

FROM scratch AS export-stage
COPY --from=build_stage gluten/package/target .
