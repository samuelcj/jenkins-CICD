#!/usr/bin/env groovy

def call() {
    echo "Building the application"
    sh "mvn package"
    echo "Deploying to ${Environment}"
    echo "Building the docker image..."
    echo "Executing pipeline for branch $BRANCH_NAME"
} 