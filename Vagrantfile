# -*- mode: ruby -*-
# vi: set ft=ruby :

TERRAFORM_VERSION = "0.7.0"

AWS_ACCESS_KEY_ID = ENV["AWS_ACCESS_KEY_ID"]
AWS_SECRET_ACCESS_KEY =  ENV["AWS_SECRET_ACCESS_KEY"]

Vagrant.configure(2) do |config|
  (0..0).each do |i|
    config.vm.define "utility" do |node|

      node.vm.box = "ubuntu/trusty64"
      node.vm.hostname = "utility"

      node.vm.provision "shell", inline: "echo 'export AWS_ACCESS_KEY_ID=#{AWS_ACCESS_KEY_ID}' >> /etc/environment", privileged: true
      node.vm.provision "shell", inline: "echo 'export AWS_SECRET_ACCESS_KEY=#{AWS_SECRET_ACCESS_KEY}' >> /etc/environment", privileged: true

      node.vm.provision "shell", inline: "apt-get update -y && \
                                          apt-get install -y libffi-dev libssl-dev python-dev python-pip curl unzip && \
                                          pip install --upgrade setuptools pip && \
                                          pip install boto netaddr ansible", privileged: true

      node.vm.provision "shell", inline: "curl -sSL -o /tmp/cfssl_linux-amd64 'https://pkg.cfssl.org/R1.2/cfssl_linux-amd64' && \
                                          chmod +x /tmp/cfssl_linux-amd64  && \
                                          mv /tmp/cfssl_linux-amd64 /usr/local/bin/cfssl && \
                                          curl -sSL -o /tmp/cfssljson_linux-amd64 'https://pkg.cfssl.org/R1.2/cfssljson_linux-amd64' && \
                                          chmod +x /tmp/cfssljson_linux-amd64 && \
                                          mv /tmp/cfssljson_linux-amd64 /usr/local/bin/cfssljson", privileged: true

      node.vm.provision "shell", inline: "curl -sSLO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl && \
                                          chmod +x ./kubectl && \
                                          mv ./kubectl /usr/local/bin/kubectl", privileged: true

      node.vm.provision "shell", inline: "curl -fsSL -O 'https://releases.hashicorp.com/terraform/#{TERRAFORM_VERSION}/terraform_#{TERRAFORM_VERSION}_linux_amd64.zip' && \
                                          unzip terraform_#{TERRAFORM_VERSION}_linux_amd64.zip && \
                                          mv terraform /usr/local/bin/terraform && \
                                          chmod +x /usr/local/bin/terraform && \
                                          rm terraform_#{TERRAFORM_VERSION}_linux_amd64.zip", privileged: true
    end
  end
end
