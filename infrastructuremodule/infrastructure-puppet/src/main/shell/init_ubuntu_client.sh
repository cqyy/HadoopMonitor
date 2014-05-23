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

### install necesarry repos.
wget http://apt.puppetlabs.com/puppetlabs-release-precise.deb -O /tmp/puppetlabs-release-precise.deb
sudo dpkg -i /tmp/puppetlabs-release-precise.deb

### install puppet client
sudo apt-get update
sudo apt-get install puppet=3.4.3-1puppetlabs1 -y


### so we can kick from puppet master
cat >> /etc/puppet/puppet.conf <<END
    listen = true
END

### ensure puppet server is in client configuration
sudo sed -i "/\[main\]/aserver = $puppet_server_hostname" /etc/puppet/puppet.conf 

### get configurations from puppetmaster
# fqdn of selfmachine
sudo puppet resource host $hostname ip="$ipaddress"
# fqdn of puppetmaster
sudo puppet resource host "$puppet_server_hostname" ip=$puppet_server_ipaddress
# init puppet agent 
sudo puppet agent --test --server "$puppet_server_hostname"
