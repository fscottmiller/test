def prepareSlaves(String projectName, String projectRepository, String projectBranch, String projectEnvironment) {
	def configData = readYaml(file: 'test_dir/jenkins-config.yml')
	def nodeLabel = configData['language']
	def builds = [:]
	def onlineCounter = 0
	//for each jenkins slave
	for(computer in Jenkins.instance.computers) {
		//if computer has label matching 'nodeLabel'
		if (computer.isOnline() && computer.getAssignedLabels().findAll({it.name == nodeLabel}).size() > 0) {
			onlineCounter++
				
			//for each executor in slave
			for (int executorNum = 0; executorNum < computer.getNumExecutors(); executorNum++) {
				def computerName = computer.getName()
				// creates build corresponding to slave and executor number
				builds["${computerName}_${executorNum}"] = {
					//build on specified node
					//logical error: if 2 pipelines are running at the same time, both use an agent with 
					// 		 2+ executors, and pipeline A's test job is not finished before this
					//  		 step takes place, this will prepare the same executor twice
					node(computerName) {
						checkout([
							 $class: 'GitSCM',
							 branches: [[name: "*/${projectBranch}"]],
							 doGenerateSubmoduleConfigurations: false,
							 userRemoteConfigs: [[credentialsId: 'GitConnector_QATInc', url: "${projectRepository}"]]
						])
						sh('mkdir -p reports')
					}
				}
			}
		}
	}
	if (onlineCounter > 0) {
		parallel(builds)
	} else {
		error("No online nodes matching label: [${nodeLabel}]")
	}
	
}

def executeTests(String projectName, String projectRepository, String projectBranch, String projectEnvironment) {
	def configData = readYaml(file: 'test_dir/jenkins-config.yml')
	def envTag = projectEnvironment
	def functionTags = configData['environments'][projectEnvironment]['stages']
	def nodeLabel = configData['language']
	def invalidTag = 'ToDo'
	
	//for each functional tag specified in jenkins-config.yml for given environemnt
	for(functionTag in functionTags) {
		//creates a jenkins stage
		stage(functionTag) {
			//tagging logic for the stage
			def tagLogic = "@${envTag} and @${functionTag} and not @${invalidTag}"
			def isFailed = false
			//list of feature files to be run
			def features = []
			//hash of builds to be executed
			def builds = [:]
			
			//populates the features list with the dry-run results
			node(nodeLabel) {
				def dryrunSuccess = sh(script: "cucumber --dry-run --tags '${tagLogic}' --format json --out dry-run.json", returnStdout: true)
				for(feature in readJSON(file: 'dry-run.json')) {
					features << feature.uri
				}
			}
			
			def stageResult = "Pipeline executing features matching tags: '${tagLogic}'"
			//for each feature in the features list
			for(feature in features) {
				def featureFile = feature
				def featureName = feature.substring(feature.lastIndexOf('/') + 1, feature.lastIndexOf('.'))
				//unique name for feature
				def runName = "${featureName}-${projectEnvironment}-${functionTag}"
				
				//populates the builds hash with a build for each feature
				builds[featureName] = {
					//run on 'nodelabel'
					node(nodeLabel) {
						//runs the cucumber tests and stores results
						def buildSuccess = sh(script: "cucumber --tags '${tagLogic}' '${featureFile}' --format json --out 'reports/${runName}.json' TEST_ENV=${projectEnvironment}", returnStatus: true) == 0
						
						//preparing report JSON for ELK consumption
						try {
							def reportJSON = readJSON(file: "reports/${runName}.json")
							reportJSON[0]['name'] += " [Tags: ${tagLogic}]"
							reportJSON[0]['timestamp'] = new Date().toTimestamp().toString()
							writeJSON(file: "reports/${runName}.json", json: reportJSON)
							sh("echo.>>reports/${runName}.json")
							//copies report JSON back to master
							archiveArtifacts(artifacts: 'reports/*.json')
						} catch(java.io.FileNotFoundException e1) {
							buildSuccess = false;
							println("Report JSON for ${featureName} did not get generated")
						}
						
						//printing pass/fail for feature
						if(buildSuccess) {
							stageResult += "\r\n SUCCESS: ${featureFile}"
						} else {
							isFailed = true;
							stageResult += "\r\n FAILURE: ${featureFile}"
						}
					}
				}			
			}
			//sends the build hash to be executed
			parallel(builds)
			
			if(isFailed) {
				currentBuild.setResult('FAILURE')
				error(stageResult)
			} else {
				println(stageResult)
			}
		}
		
	}
}

return this;
