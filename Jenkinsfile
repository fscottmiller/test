@Library('lib')_

pipeline {
    agent { node { label 'master' } }
    
    parameters {
        string(name: 'Repository', defaultValue: 'http://github.com/fscottmiller/test-suite', description: 'The repo containing the testing code you want to run')
        string(name: 'Branch', defaultValue: 'master', description: 'The branch of your repo in which you are interested')
        string(name: 'Environment', defaultValue: 'dev', description: 'The environment you are interested in')
    }

    stages {
        stage ('Prepare Environment') {
            steps {
				prepareMaster(params.Repository, params.Branch)
				prepareSlaves(params.Repository, params.Branch)
            }
        }
        stage ('Execute Tests') {
            steps {
                echo "run tests"
                executeTests(params.Environment)
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
