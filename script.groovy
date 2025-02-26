def testApp() {
    echo "testing the application..."
    sh "mvn test"
} 

def buildApp() {
    echo "Building the application"
    sh "mvn package"
    echo "Deploying to ${Environment}"
    echo "Building the docker image..."
    echo "Executing pipeline for branch $BRANCH_NAME"
} 

def deployApp() {
    env.ENV = input message: "Select the evironment for the deployment", ok: "Done", parameters: [choice(name: "ENV", choices: ["dev", "prod"], description: "Selecting Environment")]
    echo "The  Deployment Environment has been set to ${ENV}"
    echo "Deploying the application..."
    withCredentials([usernamePassword(credentialsId: "DockerHub", passwordVariable: "PASSWORD", usernameVariable: "USERNAME")]) {
        sh "docker build -t samuelcj310/java-maven-app:java-maven-app_4.0 ."
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh "docker push samuelcj310/java-maven-app:java-maven-app_4.0"
    }
    echo "Successfuly deployed to ${ENV} and image pushed to the Image Repository!!!"
} 

return this
