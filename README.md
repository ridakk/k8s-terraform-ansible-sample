# Kubernetes not the hardest way (or "Provisioning a Kubernetes Cluster on AWS using Terraform and Ansible")

Original [Readme]https://github.com/opencredo/k8s-terraform-ansible-sample)

This repo has a vagrantfile to provide requirements on control machine, aims to automate setup.

`!!! k8s version in this repo is quite outdated !!!`

As the main repo is pretty old and there is `kube-dns` configuration, pod-to-internet connection was not working.

Tried updating k8s version but current version is too old to be upgraded easily.

But i managed to fix pod-to-internet issue with adding below lines to deployment definitions

```yaml
      hostNetwork: true
      dnsPolicy: ClusterFirst
```

## AWS Credentials

### AWS KeyPair

Place the `.pem` into project repository by renaming it as `vagrant-aws-k8s-key-pair.pem`.
(All `.pem` files are ignored but be sure not to upload it ro remote repository)

### Terraform and Ansible authentication

Set AWS credentials on host machine, vagrant controller machine will inherit them automatically.

```bash
export AWS_ACCESS_KEY_ID=<access-key-id>
export AWS_SECRET_ACCESS_KEY=<secret-key>
```

## Requirements

Creating control machine:

- vagrant
- virtual box

Once vagrant machine is ready, you are ready to start using terraform and ansible

```bash
vagrant up
```

### SSH to control machine

Ssh to control machine and switch to root user

```bash
vagrant ssh
sudo su
```

## Provision infrastructure

```bash
root@utility:/vagrant/terraform# terraform plan
root@utility:/vagrant/terraform# terraform apply

root@utility:/vagrant/ansible# ansible-playbook infra.yaml
root@utility:/vagrant/ansible# ansible-playbook kubectl.yaml --extra-vars "kubernetes_api_endpoint=kubernetes-1593228413.eu-west-1.elb.amazonaws.com"
root@utility:/vagrant/ansible# ansible-playbook kubernetes-routing.yaml
root@utility:/vagrant/ansible# ansible-playbook kubectl-jenkins-sonar.yaml
```

## Accessing Jenkins and Sonar

Check which worker Jenkins and Sonar deployed

```bash
root@utility:/vagrant/ansible# kubectl get pods -o wide
NAME                         READY   STATUS    RESTARTS   AGE     IP           NODE                                       NOMINATED NODE   READINESS GATES
jenkins-1529594462-xf7oa     1/1     Running   0          3h45m   10.43.0.31   ip-10-43-0-31.eu-west-1.compute.internal   <none>           <none>
sonarqube-2261369614-moa3p   1/1     Running   0          3h48m   10.43.0.30   ip-10-43-0-30.eu-west-1.compute.internal   <none>           <none>
```

Check public ip of the instance and login from browser

for jekins: http://<worker-ip>:8080
for sonar: http://<worker-ip>:9000

## Jenkins Plugins and Pipelines

All plugins are listed in `jenkins/plugins.txt` and bundled in docker image while building via `jenkins/Dockerfile`

Jenkins configuration and plugin setups are done via groovy scripts under `jenkins/groovy` as well as creating jobs

So Jenkins is completely disposable unless if you want to keep job history

To change admin user password and github token, update `jenkins/groovy/add-credentials.groovy` and `jenkins/groovy/basic-security.groovy`

## SonarQube configuration

Need to update SonarQube server url and auth token manually once sonar is up and running

- login to sonar with admin:admin
- create new user
- loging with new user and create a token
- update SonarQube configuration token
- update SonarQube server url

`Need to check how to automate the flow above.`

## Test App

A simple nodejs app is under `test-app`, has its own Dockerfile and used to test k8s deployment via Jenkins

But the SonarScanner runs one of my public github repositories.
