def prepareEnvironment(String projectName, String projectRepository, String projectBranch, String projectEnvironment) {
	checkout([
         $class: 'GitSCM',
         branches: [[name: "*/${projectBranch}"]],
         doGenerateSubmoduleConfigurations: false,
         extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'test_dir']],
         userRemoteConfigs: [[credentialsId: 'GitConnector_QATInc', url: "${projectRepository}"]]
    ])
	def configData = readYaml(file: 'test_dir/jenkins-config.yml')
	def projectLanguage = configData['language']
	def runner = null
	switch(projectLanguage) {
		case 'ruby':
			runner = load('lib_groovy/ruby.groovy')
			break
		case 'junit':
			runner = load('lib_groovy/junit.groovy')
			break
		case 'specflow':
			runner = load('lib_groovy/specflow.groovy')
			break
		default:
			error('Project language from jenkins-config.yml is not correct/supported')
			break
	}
	runner.prepareSlaves(projectName, projectRepository, projectBranch, projectEnvironment)
}

def executeTests(String projectName, String projectRepository, String projectBranch, String projectEnvironment) {
	def configData = readYaml(file: 'test_dir/jenkins-config.yml')
	def projectLanguage = configData['language']
	def runner = null
	switch(projectLanguage) {
		case 'ruby':
			runner = load('lib_groovy/ruby.groovy')
			break
		case 'junit':
			runner = load('lib_groovy/junit.groovy')
			break
		case 'specflow':
			runner = load('lib_groovy/specflow.groovy')
			break
		default:
			error('Project language from jenkins-config.yml is not correct/supported')
			break
	}
	runner.executeTests(projectName, projectRepository, projectBranch, projectEnvironment)
}

def sendNotifications(String projectName, String projectRepository, String projectBranch, String projectEnvironment) {
	
}

return this;