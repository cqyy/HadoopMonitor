#!/bin/sh
set -x

hostname=$(hostname -f)
# Assuming server is CentOS system.
# epel repo
rpm -ivh http://download.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
# puppetlabs repo
rpm -ivh https://yum.puppetlabs.com/el/6/products/x86_64/puppetlabs-release-6-7.noarch.rpm
# Installation begins
yum install -y puppet puppet-server puppetdb puppet-dashboard puppetdb-terminus

# puppetdb,puppet master configuration
cat >/etc/puppet/puppetdb.conf <<END 
[main]
    server = $hostname
    port = 8081
END

lokkit -p 8139:tcp
cat >> /etc/puppet/puppet.conf <<END
    listen = true
[master]
    storeconfigs = true
    storeconfigs_backend = puppetdb
    reports = store, puppetdb
END

cat > /etc/puppet/routes.yaml <<END
---
master:
  facts:
    terminus: puppetdb
    cache: yaml
END

# try start, and generate SSLs.
service puppetdb start
service puppetmaster start
service puppetdb stop
service puppetmaster stop

# init puppetdb
/usr/sbin/puppetdb ssl-setup

### ports
lokkit -s http
lokkit -p 8140:tcp

### pip domestic mirror
mkdir -p /root/.pip/
cat > /root/.pip/pip.conf <<EOF
[global]
index-url=http://pypi.mirrors.ustc.edu.cn/simple
EOF

# use puppet to manage puppet
puppet resource service puppetdb ensure=running enable=true
puppet resource service puppetmaster ensure=running enable=true
puppet resource service puppet ensure=running enable=true
# Initialization complete.
