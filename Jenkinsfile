@Library('lib')

pipeline {
    agent { node { label 'master' } }z
    
    //parameters {
     //   string(name: 'project_name', description: '', defaultValue: '')
	//	string(name: 'project_repo', description: '', defaultValue: '')
	//	string(name: 'project_branch', description: '', defaultValue: 'master')
	//	string(name: 'project_env', description: '', defaultValue: '')
    //}
    
    stages {
        stage ('Checkout SCM') {
            steps {
                checkout scm
            }
        }
        stage ('Prepare Environment') {
            prepareEnvironment()
        }
        stage ('Test') {
            executeTests()
        }
    }
    post {
        always {
            echo 'Sending emails...'
        }
    }
}
