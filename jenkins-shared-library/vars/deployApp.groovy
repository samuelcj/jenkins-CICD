#!/usr/bin/env groovy

def call(String DockerRepo ImageTag) {
    env.ENV = input message: "Select the evironment for the deployment", ok: "Done", parameters: [choice(name: "ENV", choices: ["dev", "prod"], description: "Selecting Environment")]
    echo "The  Deployment Environment has been set to ${ENV}"
    echo "Deploying the application..."
    withCredentials([usernamePassword(credentialsId: "DockerHub", passwordVariable: "PASSWORD", usernameVariable: "USERNAME")]) {
        sh "docker build -t $DockerRepo:$ImageTag ."            // Usind varriables that will be specified in the jenkins file
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh "docker push samuelcj310/java-maven-app:java-maven-app_4.0"
    }
    echo "Successfuly deployed to ${ENV} and image pushed to the Image Repository!!!"
} 
