#!/usr/bin/env bash

buildProdDockerImage() {
    sbt dist

    unzip target/universal/*.zip
    rm -rf video-broadcast-api
    mv video-broadcast-api* video-broadcast-api

    docker build -t video-broadcast-api -f deploy/Dockerfile-prod .

    rm -rf video-broadcast-api
}
