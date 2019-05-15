#!groovy

import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()

def hudsonRealm = new HudsonPrivateSecurityRealm(false)

// TODO: need to move this password to env or somewhere else
hudsonRealm.createAccount("admin","c46UqwaCxhKTxGbn")
instance.setSecurityRealm(hudsonRealm)
instance.save()
