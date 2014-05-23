class nagios::monitor {
	package { [ nagios,httpd ]: ensure => installed }
	service { httpd:
		ensure => running,
		enable => true,
		require => Package['httpd'],
	}
	service { nagios:
		ensure => running,
		enable => true,
		require => Package['nagios'],
	}
	file_line { "nagios_command.cfg":
		line => "cfg_file=/etc/nagios/nagios_command.cfg",
		path => "/etc/nagios/nagios.cfg",
		ensure => present,
	}
	file_line { "nagios_service.cfg":
		line => "cfg_file=/etc/nagios/nagios_service.cfg",
		path => "/etc/nagios/nagios.cfg",
		ensure => present,
	}
	file_line { "nagios_host.cfg":
		line => "cfg_file=/etc/nagios/nagios_host.cfg",
		path => "/etc/nagios/nagios.cfg",
		ensure => present,
	}
	file { "/etc/nagios/nagios_host.cfg":
		mode => 666,
		owner => root,
		group => root,
		ensure => file,
		require => Package['nagios'],
		notify => Service['nagios'],
	}
	file { "/etc/nagios/nagios_service.cfg":
		mode => 666,
		owner => root,
		group => root,
		ensure => file,
		require => Package['nagios'],
		notify => Service['nagios'],
	}
	Nagios_host <<| |>>
	Nagios_service <<| |>>
}
