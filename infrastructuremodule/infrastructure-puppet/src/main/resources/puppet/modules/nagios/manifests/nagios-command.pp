class nagios::nagios-command {
	package { "nagios-plugins-nrpe" :
		ensure => installed,
	}
	nagios_command { 'basic_nagios_command_check_nrpe':
		command_name => 'check_nrpe',
		ensure       => 'present',
		command_line => '/usr/lib64/nagios/plugins/check_nrpe -H $HOSTADDRESS$ -c $ARG1$',
	}
	file { "nagios_command":
		path => "/etc/nagios/nagios_command.cfg",
		mode => 666,
		notify => Service['nagios']
	}
}
