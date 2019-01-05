@Library('lib')_

pipeline {
    agent { node { label 'master' } }
    
    parameters {
        string(name: 'Repository', defaultValue: 'http://github.com/fscottmiller/test-suite', description: 'The repo containing the testing code you want to run')
        string(name: 'Branch', defaultValue: 'master', description: 'The branch of your repo in which you are interested')
        string(name: 'Environment', defaultValue: 'dev', description: 'The environment you are interested in')
    }
	
	environment {
		ConfigurationFile = "jenkins-config.yml"
	}

    stages {
        stage ('Prepare Environment') {
            steps {
				prepareMaster(Repository, Branch, ConfigurationFile)
				prepareSlaves(Repository, Branch)
            }
        }
        stage ('Execute Tests') {
            steps {
                echo "run tests"
                executeTests(Environment)
            }
        }
    }

    post {
        always {
            echo 'Sending emails...'
			sendEmails()
			//exportReports()
            cleanWs()
        }
    }
}
