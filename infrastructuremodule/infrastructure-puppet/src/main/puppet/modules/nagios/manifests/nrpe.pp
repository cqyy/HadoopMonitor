class nagios::nrpe {
	$nrpe_server_ip = '172.16.187.129'
	$nrpe_service = $operatingsystem ? {
		centos => 'nrpe',
		redhat => 'nrpe',
		/(?i)(ubuntu|debian)/ => 'nagios-nrpe-server',
		default               => undef,
	}
	if $operatingsystem == centos {
		package { [ nrpe, nagios-plugins, nagios-plugins-all, perl-Sys-Statistics-Linux ]:
			ensure => installed,
		}
	} elsif $operatingsystem == ubuntu {
		package { [ openssl, nagios-nrpe-server, nagios-plugins, nagios-plugins-basic, nagios-plugins-standard, libsys-statistics-linux-perl ]:
			ensure => installed, }
	}
	service { $nrpe_service :
		ensure => running,
		enable => true,
		require => Package["$nrpe_service"],
	}
	$utils_pm_32bit = file('/usr/lib/nagios/plugins/utils.pm','/dev/null')
	$utils_pm_64bit = file('/usr/lib64/nagios/plugins/utils.pm','/dev/null')
	if ($utils_pm_32bit != '') {
		file  { '/opt/utils.pm':
			content => $utils_pm_32bit,
			mode => 0644,
			owner => 'root',
			group => 'root',
		}
	}
	if ($utils_pm_64bit != '') {
		file  { '/opt/utils.pm':
			content => $utils_pm_64bit,
			mode => 0644,
			owner => 'root',
			group => 'root',
		}
	}
	file_line { "nrpe_command_check_disk":
		path => '/etc/nagios/nrpe.cfg',
		line => 'command[check_disk]=/opt/check_linux_stats.pl -D -w 10 -c 5 -p /,/home,/var -u %',
		require => File["/opt/check_linux_stats.pl"],
	}
	file_line { "nrpe_command_check_mem":
		path => '/etc/nagios/nrpe.cfg',
		line => 'command[check_mem]=/opt/check_linux_stats.pl -M -w 100,25 -c 100,50',
		require => File_line["nrpe_command_check_disk"],
	}
	file_line { "nrpe_command_check_cpu":
		path => '/etc/nagios/nrpe.cfg',
		line => 'command[check_cpu]=/opt/check_linux_stats.pl -C -w 90 -c 100 -s 5',
		require => File_line["nrpe_command_check_mem"],
	}
	file_line { "nrpe_command_check_open_file":
		path => '/etc/nagios/nrpe.cfg',
		line => 'command[check_open_file]=/opt/check_linux_stats.pl -F -w 10000,250000 -c 15000,350000',
		require => File_line["nrpe_command_check_cpu"],
	}
	file_line { "nrpe_command_check_io":
		path => '/etc/nagios/nrpe.cfg',
		line => 'command[check_io]=/opt/check_linux_stats.pl -I -w 2000,600 -c 3000,800 -p sda1,sda2,sda3 -s 5',
		require => File_line["nrpe_command_check_open_file"],
	}
	file_line { "nrpe_command_check_proc":
		path => '/etc/nagios/nrpe.cfg',
		line => 'command[check_proc]=/opt/check_linux_stats.pl -P -w 1000 -c 2000',
		require => File_line["nrpe_command_check_io"],
	}
	file_line { "nrpe_command_check_net":
		path => '/etc/nagios/nrpe.cfg',
		line => 'command[check_net]=/opt/check_linux_stats.pl -N -w 1000000 -c 1500000 -p eth0,eth1,eth2,eth3,eth4',
		require => File_line["nrpe_command_check_proc"],
	}
	file_line { "nrpe_command_check_socket":
		path => '/etc/nagios/nrpe.cfg',
		line => 'command[check_socket]=/opt/check_linux_stats.pl -S -w 500 -c 1000',
		require => File_line["nrpe_command_check_net"],
	}
	file_line { "nrpe_command_check_uptime":
		path => '/etc/nagios/nrpe.cfg',
		line => 'command[check_uptime]=/opt/check_linux_stats.pl -U -w 5',
		require => File_line["nrpe_command_check_socket"],
	}
	file_line { "allowed_hosts":
		line => "allowed_hosts = 127.0.0.1,$nrpe_server_ip",
		path => "/etc/nagios/nrpe.cfg",
		match => "allowed_hosts",
		ensure => present,
		require => File_line["nrpe_command_check_uptime"],
		notify  => Service["$nrpe_service"],
	}
	file { "/opt/check_linux_stats.pl":
		mode => 755,
		owner => root,
		group => root,
		source => "puppet:///modules/nagios/check_linux_stats.pl"
	}
}
