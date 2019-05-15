import jenkins.model.Jenkins
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.biouno.unochoice.model.GroovyScript
import org.biouno.unochoice.ChoiceParameter
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript
import org.biouno.unochoice.CascadeChoiceParameter
import hudson.model.ParametersDefinitionProperty

jenkins = Jenkins.instance

if (jenkins.getItem("sonar-test")) {
	println("sonar-test job exists")
  return
}

WorkflowJob job = jenkins.createProject(WorkflowJob,  "sonar-test")
job.setDisplayName('Sonar test')

String fileContents = new File('/var/jenkins_home/pipelines/sonartest/Jenkinsfile').getText('UTF-8')
job.definition = new CpsFlowDefinition(fileContents, Boolean.TRUE)

