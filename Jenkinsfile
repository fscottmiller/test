@Library('lib')_

pipeline {
    agent { node { label 'master' } }
    
    parameters {
        string(name: 'Repository', defaultValue: '', description: 'The repo containing the testing code you want to run')
    }
    
    stages {
        stage ('Checkout SCM') {
            steps {
                bat "git clone ${params.Repository}"
                }
        }
        stage ('Prepare Environment') {
            steps {
                prepareEnvironment()
            }
        }
        stage ('Test') {
            steps {
                executeTests()
            }
        }
    }
    post {
        always {
            echo 'Sending emails...'
            cleanWs()
        }
    }
}
