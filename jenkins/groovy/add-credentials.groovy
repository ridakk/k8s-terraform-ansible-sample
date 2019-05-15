import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import hudson.util.Secret
import jenkins.model.*
import hudson.security.*
domain = Domain.global()
store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

// TODO: need to move this credentials to env or somewhere else
GITHUB_TOKEN = new StringCredentialsImpl(CredentialsScope.GLOBAL,"GITHUB_TOKEN","GITHUB_TOKEN",Secret.fromString("462a235be1ef83dad6f91c812e4f9dac12562b3a"))
KUBECTL = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL,"KUBECTL", "KUBECTL","admin","chAng3m3")

store.addCredentials(domain, GITHUB_TOKEN)
store.addCredentials(domain, KUBECTL)
