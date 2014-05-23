#!/bin/bash
set -x
### puppet master hostname
puppet_server_hostname=""
puppet_server_ipaddress=""

if [ -z $puppet_server_hostname ]; then
	echo "puppet_server_hostname not set!"
	exit
fi
if [ -z $puppet_server_ipaddress ]; then
	echo "puppet_server_ipaddress not set!"
	exit
fi

### client props
hostname=$(hostname -f)
## all interfaces should be checked to find 192.168.[0-9].x
re="192\.168\.[0-9]\..*"
ips=$(ifconfig | grep "inet " | awk '{print $2}' | cut -d: -f2)
for ip in $ips
    do
        if [[ $ip =~ $re ]];then
            ipaddress=$ip
            break
        fi
done

if [ -z $ipaddress ]; then
    echo "no proper lan address"
    exit
fi

### nrpe 5666
lokkit -p 5666:tcp


### install necesarry repos.
rpm -ivh http://download.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
rpm -ivh https://yum.puppetlabs.com/el/6/products/x86_64/puppetlabs-release-6-7.noarch.rpm

### install puppet client
yum -y install puppet
### so we can kick from puppet master
cat >> /etc/puppet/puppet.conf <<END
    listen = true
END
lokkit -p 8139:tcp

### ensure puppet server is in client configuration
sed -i "/\[main\]/a\ \ \ \ server = $puppet_server_hostname" /etc/puppet/puppet.conf 

### get configurations from puppetmaster
puppet resource host $hostname ip="$ipaddress"
puppet resource host "$puppet_server_hostname" ip=$puppet_server_ipaddress

puppet agent --test --server "$puppet_server_hostname"