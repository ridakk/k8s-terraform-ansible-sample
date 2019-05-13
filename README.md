# Kubernetes not the hardest way (or "Provisioning a Kubernetes Cluster on AWS using Terraform and Ansible")

Original [Readme]https://github.com/opencredo/k8s-terraform-ansible-sample)

This repo has a vagrantfile to provide requirements on control machine, aims to automate setup.

## AWS Credentials

### AWS KeyPair

You need a valid AWS Identity (`.pem`) file and the corresponding Public Key. Terraform imports the [KeyPair](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html) in your AWS account. Ansible uses the Identity to SSH into machines.

Please read [AWS Documentation](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html#how-to-generate-your-own-key-and-import-it-to-aws) about supported formats.

Place the `.pem` into project repository by renaming it as `vagrant-aws-k8s-key-pair.pem`.
(All `.pem` files are ignored but be sure not to upload it ro remote repository)

### Terraform and Ansible authentication

Both Terraform and Ansible expect AWS credentials set in environment variables:

```bash
export AWS_ACCESS_KEY_ID=<access-key-id>
export AWS_SECRET_ACCESS_KEY="<secret-key>"
```

## Requirements

Creating control machine:

- vagrant
- virtual box

Once vagrant machine is ready, you are ready to start using terraform and ansible

```bash
vagrant up
```

`Follow configuration in original post if required`.

### SSH to control machine

Ssh to control machine and switch to root user

```bash
vagrant ssh
sudo su
```

## Provision infrastructure, with Terraform

Run Terraform commands from `./terraform` subdirectory.

```bash
terraform plan
terraform apply
```

### Install and set up Kubernetes cluster

Install Kubernetes components and *etcd* cluster.

```bash
ansible-playbook infra.yaml
```

If you receive (public-key denied) error, try re-adding for `.pem` as below and double check if your private ad public key matches.

```bash
eval $(ssh-agent)
ssh-add /vagrant/vagrant-aws-k8s-key-pair.pem
```

### Setup Kubernetes CLI

Configure Kubernetes CLI (`kubectl`) on your machine, setting Kubernetes API endpoint (as returned by Terraform).

```bash
ansible-playbook kubectl.yaml --extra-vars "kubernetes_api_endpoint=<kubernetes-api-dns-name>"
```

### Setup Pod cluster routing

Set up additional routes for traffic between Pods.

```bash
ansible-playbook kubernetes-routing.yaml
```

### Deploy Jenkins and SonarQube

!!! Jenkins seems to be broken https://github.com/jenkinsci/docker/issues/681 !!!

Deploy a *jenkins* and *sonar* service inside Kubernetes.

```bash
ansible-playbook kubectl-jenkins-sonar.yaml
```

Verify pods and service are up and running.

```bash
$ kubectl get pods -o wide
NAME                        READY   STATUS    RESTARTS   AGE     IP           NODE                                       NOMINATED NODE   READINESS GATES
jenkins-1308212290-skyp4    1/1     Running   0          2m21s   10.200.2.2   ip-10-43-0-32.eu-west-1.compute.internal   <none>           <none>
sonarqube-212583104-vdlql   1/1     Running   0          101s    10.200.0.2   ip-10-43-0-30.eu-west-1.compute.internal   <none>           <none>
$ kubectl get services
NAME         TYPE        CLUSTER-IP    EXTERNAL-IP   PORT(S)          AGE
jenkins      NodePort    10.32.0.195   <none>        8080:31010/TCP   5m32s
kubernetes   ClusterIP   10.32.0.1     <none>        443/TCP          12m
sonarqube    NodePort    10.32.0.69    <none>        9000:31020/TCP   105s

```

