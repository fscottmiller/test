@Library('lib')_

pipeline {
    agent { node { label 'master' } }
    
    parameters {
        string(name: 'Repository', defaultValue: '', description: 'The repo containing the testing code you want to run')
        string(name: 'Branch', defaultValue: 'master', description: 'The branch of your repo in which you are interested') 
    }
    
    stages {
        stage ('Prepare Environment') {
            steps {
                bat "ruby -v"
                gitClone(params.Repository, params.Branch)
                prepareEnvironment()
            }
        }
        stage ('Test') {
            steps {
                executeTests("@web")
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
