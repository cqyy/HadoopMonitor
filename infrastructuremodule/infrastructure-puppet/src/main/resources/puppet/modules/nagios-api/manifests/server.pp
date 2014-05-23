class nagios-api::server {
	package { [ 'python-pip', 'python-devel', 'python-virtualenv', 'libffi-devel', 'gcc-c++', 'openssl-devel' ]:
		ensure => installed,
	}
	python::pip::install { ['cffi', 'Flask','Werkzeug','Jinja2','Twiggy','diesel','dnspython','greenlet','http-parser','pyOpenSSL','requests','nagios-api']:
		venv => '/usr',
		ensure => present,
		require => Package['libffi-devel','gcc-c++', 'python-devel', 'openssl-devel'],
	}
	exec { "run_nagios-api":
		path => "/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/root/bin",
		command => "nohup /usr/bin/nagios-api -s /var/log/nagios/status.dat -l /var/log/nagios/nagios.log >/var/log/nagios/nagios-api.log 2>&1 &",
		unless => "netstat -nao | grep -c 6315",
	}
}
