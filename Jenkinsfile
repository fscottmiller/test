pipeline {
    agent { node { label 'master' } }
    
    parameters {
        string(name: 'project_name', description: '', defaultValue: '')
		string(name: 'project_repo', description: '', defaultValue: '')
		string(name: 'project_branch', description: '', defaultValue: 'master')
		string(name: 'project_env', description: '', defaultValue: '')
    }
    
    stages {
        stage ('Checkout SCM') {
            steps {
                checkout scm
            }
        }
        stage ('Get Configuration') {
            steps {
                echo 'Configuring...'
                script {
                    def configData = readYaml file: 'config.yml'
                }
            }
        }
        stage ('Prepare Environment') {
            steps {
                echo "${configData}"
            }
        }
        stage ('Test') {
            steps {
                echo 'Testing...'
            }
        }
        stage ('Deploy') {
            steps {
                echo 'Deploying...'
            }
        }
    }
    post {
        always {
            echo 'Sending emails...'
        }
    }
}