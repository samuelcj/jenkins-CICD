# Jenkins CI/CD Project

Jenkins is an open-source automation server that enables developers to build, test, and deploy software efficiently and reliably. It serves as a critical component in the DevOps pipeline, offering integration and automation capabilities for a wide variety of tools and technologies.

## Key Features of Jenkins
- **Automation**: Automates tasks like building, testing, and deploying applications.
- **Extensibility**: Offers a rich ecosystem of plugins to integrate with virtually any tool in the development lifecycle.
- **Scalability**: Supports distributed builds across multiple machines to enhance performance.
- **Flexibility**: Compatible with numerous platforms and environments, from local setups to cloud infrastructure.
- **CI/CD Support**: Facilitates Continuous Integration (CI) and Continuous Delivery/Deployment (CD), ensuring faster and more reliable software releases.

## Installing Jenkins
### Option 1: Installing on a Dedicated Server (Amazon EC2)
1. Launch an EC2 instance and create a Jenkins user.
2. Install Jenkins manually by following [official documentation](https://www.jenkins.io/doc/book/installing/).

### Option 2: Running Jenkins as a Docker Container (Recommended)
1. Launch an EC2 instance and ensure port `8080` is open.
2. SSH into the instance:  
   ```sh
   ssh -i <key-pair> ec2-user@<public IP>
   ```
3. Run the Jenkins container:
   ```sh
   docker run -p 8080:8080 -p 50000:50000 -d -v jenkins_data:/var/jenkins_home jenkins/jenkins:lts
   ```
   - `-p 8080:8080` → Access Jenkins UI.
   - `-p 50000:50000` → Communication between master and worker nodes.
   - `-v jenkins_data:/var/jenkins_home` → Data persistence.

4. Retrieve the initial admin password:
   ```sh
   docker exec -it <container ID> cat /var/jenkins_home/secrets/initialAdminPassword
   ```
5. Install plugins and create the first admin user after logging in.

## Jenkins Jobs
### Automating Java Maven App Workflow
1. Install Maven via Jenkins UI system configuration.
2. Configure Jenkins to run tests and build the `.jar` file.

### Automating Node.js App Workflow
1. Install Node.js and NPM manually inside the Jenkins container:
   ```sh
   docker exec -u 0 -it <container ID> bash
   apt update && apt install -y curl
   curl -sL https://deb.nodesource.com/setup_10.x | bash -
   apt install -y nodejs
   ```
2. Verify installation:
   ```sh
   node -v && npm -v
   ```
3. Add necessary plugins from Jenkins plugin manager.

## Configuring Git Repository
- Configure jobs to pull from a Git repository.
- Ensure the correct permissions and credentials for the Jenkins user.

## Running Tests and Building Java Maven Applications
1. Configure Jenkins to checkout code from a repository.
2. Ensure the `pom.xml` file path is set correctly.

## Running Docker in Jenkins
1. Run Jenkins with Docker socket access:
   ```sh
   docker run -p 8080:8080 -p 50000:50000 -d \
   -v jenkins_data:/var/jenkins_home \
   -v /var/run/docker.sock:/var/run/docker.sock \
   -v $(which docker):/usr/bin/docker jenkins/jenkins:lts
   ```
2. Allow Jenkins to execute Docker commands:
   ```sh
   docker exec -u 0 -it <container ID> bash
   chmod 666 /var/run/docker.sock
   ```

## Building and Pushing Docker Images
### Building Docker Images
- Add a shell script build step:
  ```sh
  docker build -t <image name>:<image version> ./<path-to-dockerfile>
  ```

### Pushing Images to Docker Hub
1. Create a Docker Hub account and configure Jenkins credentials.
2. Modify the build script:
   ```sh
   docker build -t <dockerhub-repo>:<tag> ./
   echo $PASSWORD | docker login -u $USERNAME --password-stdin
   docker push <dockerhub-repo>:<tag>
   ```

### Pushing Images to External Repositories (e.g., Nexus, Amazon ECR)
1. Configure credentials in Jenkins.
2. Modify the build script:
   ```sh
   docker build -t <repository>:<tag> ./
   echo $PASSWORD | docker login -u $USERNAME --password-stdin <repository-host>
   docker push <repository>:<tag>
   ```

## Jenkins Pipeline Jobs
### Creating a Jenkinsfile
Jenkinsfiles use Groovy syntax and should be stored in the project repository.
#### Example Declarative Pipeline:
```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker build -t my-app:1.0 .'
                sh 'docker push my-app:1.0'
            }
        }
    }
}
```

### Multi-Branch Pipelines
- Supports branching strategies and automatic job creation per branch.
- Enables dynamic pipeline execution based on branch logic.

## Credentials in Jenkins
1. **System Scope**: Available only on the Jenkins server.
2. **Global Scope**: Accessible to all jobs.
3. **Credential Binding Plugin**: Used to securely reference credentials in pipelines.

## Shared Libraries
- Shared Groovy libraries help centralize and reuse pipeline logic across projects.
- Configure under `Global Trusted Pipeline Libraries` in Jenkins settings.
- Example usage:
  ```groovy
  @Library('jenkins-shared-library') _
  mySharedLibraryFunction()
  ```

## Automatically Triggering Jenkins Builds
1. Install the required build trigger plugin (e.g., GitHub, GitLab).
2. Configure the SCM plugin in Jenkins system settings.
3. Generate an API token from GitHub (Personal Access Token).
4. Use the **Webhook URL** in your repository settings to trigger builds.

## Community and Support
- **Documentation**: [Jenkins Docs](https://www.jenkins.io/doc/)
- **Plugins Index**: [Jenkins Plugins](https://plugins.jenkins.io/)
- **Community Forum**: [Jenkins Community](https://community.jenkins.io/)
- **GitHub Repository**: [Jenkins GitHub](https://github.com/jenkinsci)

### Summary
This guide covers the setup and automation of Jenkins using Docker, CI/CD workflows for Java and NodeJS applications, pipeline jobs, credentials management, shared libraries, and automated build triggers. Happy DevOps-ing!

---

## License
Jenkins is licensed under the MIT License. See the [LICENSE](https://www.jenkins.io/project/jenkins-license/) for more information.
