import jenkins.model.Jenkins
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.biouno.unochoice.model.GroovyScript
import hudson.model.StringParameterDefinition
import hudson.model.ParameterDefinition
import hudson.model.ParametersDefinitionProperty

jenkins = Jenkins.instance

if (jenkins.getItem("k8s-deploy")) {
	println("k8s-deploy job exists")
  return
}

WorkflowJob job = jenkins.createProject(WorkflowJob,  "k8s-deploy")
job.setDisplayName('k8s deploy')

String fileContents = new File('/var/jenkins_home/pipelines/k8s-deploy/Jenkinsfile').getText('UTF-8')
job.definition = new CpsFlowDefinition(fileContents, Boolean.TRUE)

ParameterDefinition[] parameters = [
  new StringParameterDefinition('SERVER_URL', '', 'k8s cluster url', true),
]
job.addProperty(new ParametersDefinitionProperty(parameters))
