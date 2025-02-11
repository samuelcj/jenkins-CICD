def buildApp() {
    echo "building the application..."
    sh "mvn -f ./java-maven-app/pom.xml package"
} 

def testApp() {
    echo "Deploying to ${Environment}"
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: "DockerHub", passwordVariable: "PASSWORD", usernameVariable: "USERNAME")]) {
        sh "docker build -t samuelcj310/java-maven-app:java-maven-app_3.0 ./java-maven-app/"
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh "docker push samuelcj310/java-maven-app:java-maven-app_3.0"
    }
} 

def deployApp() {
    env.ENV = input message: "Select the evironment for the deployment", ok: "Done", parameters: [choice(name: "ENV", choices: ["dev", "prod"], description: "Selecting Environment")]
    echo "The Environment has been set to ${ENV}"
    echo "deploying the application..."
} 

return this