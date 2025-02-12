#!/usr/bin/env groovy

def call() {
    env.ENV = input message: "Select the evironment for the deployment", ok: "Done", parameters: [choice(name: "ENV", choices: ["dev", "prod"], description: "Selecting Environment")]
    echo "The  Deployment Environment has been set to ${ENV}"
    echo "Deploying the application..."
    withCredentials([usernamePassword(credentialsId: "DockerHub", passwordVariable: "PASSWORD", usernameVariable: "USERNAME")]) {
        sh "docker build -t samuelcj310/java-maven-app:java-maven-app_3.0 ."
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh "docker push samuelcj310/java-maven-app:java-maven-app_3.0"
    }
    echo "Successfuly deployed to ${ENV} and image pushed to the Image Repository!!!"
} 
