#!/usr/bin/env groovy

def gv

pipeline {
    agent any
    tools {
        maven "maven-3.9.9"
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage("test") {
            steps {
                script {
                    gv.testApp()
                }
            }
        }
        stage("build") {
            input {
                message "Select the evironment for this build"
                ok "Done"
                parameters {
                    choice(name: "Environment", choices: ["dev", "prod"], description: "Selecting Environment")
                }
            }
            steps {
                script {
                    gv.buildApp()
                }
            }
        }
        stage("deploy") {
            when {
                expression {
                    BRANCH_NAME == "main"
                }
            }
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
    }   
}