@Library('lib')_

pipeline {
    agent { node { label 'master' } }
    
    parameters {
        string(name: 'Repository', defaultValue: '', description: 'The repo containing the testing code you want to run')
    }
    
    stages {
        stage ('Clean') {
            steps {
                cleanWs()
            }
        }
        stage ('Checkout SCM') {
            steps {
                bat 'git clone https://github.com/QATInc/automation'
                bat 'dir'
                bat 'cd automation'
                bat 'dir'
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
        }
    }
}
