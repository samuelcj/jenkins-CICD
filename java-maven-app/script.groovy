def buildApp() {
    echo "building the application..."
} 

def testApp() {
    echo "building the docker image..."
    }
} 

def deployApp() {
    echo 'deploying the application...'
} 

return this             # So it can be used in the jenkins file.