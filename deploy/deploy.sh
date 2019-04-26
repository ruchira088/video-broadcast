#!/usr/bin/env bash

buildProdDockerImage() {
    sbt dist

    unzip target/universal/*.zip
    rm -rf video-broadcast
    mv video-broadcast* video-broadcast

    docker build -t video-broadcast -f deploy/Dockerfile-prod .

    rm -rf video-broadcast
}
