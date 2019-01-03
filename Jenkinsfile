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
                gitClone(params.Repository, params.Branch)
                script {
                    readConfig()
                    def conf = readYaml(file: 'config.yml')
                    env.language = conf['language']
                    env.tags = conf['tags']
                    echo "printing env.tags..."
                    echo env.tags
                    switch(env.language) {
                        case 'ruby':
                            echo 'Provisioning ruby env...'
                            // prepareRubyEnv()
                            break
                        case 'junit':
                            echo 'Provisioning java env...'
                            break
                        case 'specflow':
                            echo 'Provisioning c# env...'
                            break
                        default:
                            error('Project language from config.yml is not yet supported')
                            break
                    }
                }
            }
        }
        stage ('Test') {
            steps {
                echo "run tests"
                
                executeTests(env.language, env.tags, params.Environment)                
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
