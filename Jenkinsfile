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
                gitClone(params.Repository, params.Branch)
                script {
                    config = readYaml(file: 'config.yml')
                    env.language = config['language']
                    env.os = config['operating system']
                }
                echo env.language
                prepareEnvironment(env.language)
            }
        }
        stage ('Test') {
            steps {
                
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
