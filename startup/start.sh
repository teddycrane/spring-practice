#!/usr/bin/env bash

BUCKET_NAME=unique-bucket-name

JAR_NAME=spring-practice-0.0.1-SNAPSHOT.jar
VM_NAME=springpractice

gsutil mb gs://${BUCKET_NAME}
gsutil cp ./target/${JAR_NAME} gs://${BUCKET_NAME}/${JAR_NAME}

gcloud compute firewall-rules create ${VM_NAME}-www --allow tcp:80 --target-tags ${VM_NAME}
