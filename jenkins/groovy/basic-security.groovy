#!groovy

import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()

def hudsonRealm = new HudsonPrivateSecurityRealm(false)

hudsonRealm.createAccount("admin_name","admin_password")
instance.setSecurityRealm(hudsonRealm)
instance.save()
