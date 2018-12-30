@Library('lib')_

pipeline {
    agent { node { label 'master' } }
    
    parameters {
        string(name: 'Repository', defaultValue: '', description: 'The repo containing the testing code you want to run')
        string(name: 'Branch', defaultValue: 'master', description: 'The branch of your repo in which you are interested') 
    }
    
    stages {
        stage ('Clone Repository') {
            steps {
                gitClone(repository=params.Repository, branch=params.Branch)
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
