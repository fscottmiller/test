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
                    env.configData = readYaml file: 'config.yml'
                    env. controller = load('pipeline.groovy')
                }
            }
        }
        stage ('Prepare Environment') {
            steps {
                echo "Preparing Environment.."
                env.controller.prepareEnvironment(project_name, project_repo, project_branch, project_env)
            }
        }
        stage ('Test') {
            steps {
                echo 'Testing...'
                env.controller.executeTests(project_name, project_repo, project_branch, project_env)
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
            env.controller.sendNotifications(project_name, project_repo, project_branch, project_env)
        }
    }
}