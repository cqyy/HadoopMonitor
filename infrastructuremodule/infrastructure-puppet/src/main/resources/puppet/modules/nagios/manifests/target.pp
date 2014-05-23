class nagios::target {
	if $operatingsystem == 'centos' {
		class { 'selinux':
			mode => 'disabled',
		}
	}
	@@nagios_host { $fqdn:
		ensure =>present,
		alias => $hostname,
		address => $ipaddress,
		use => "linux-server",
	}
	@@nagios_service { "check_disk_${hostname}":
		check_command => "check_nrpe!check_disk",
		use => "generic-service",
		host_name => "$fqdn",
		service_description => "check_disk"
	}
	@@nagios_service { "check_mem_${hostname}":
		check_command => "check_nrpe!check_mem",
		use => "generic-service",
		host_name => "$fqdn",
		service_description => "check_mem"
	}
	@@nagios_service { "check_cpu_${hostname}":
		check_command => "check_nrpe!check_cpu",
		use => "generic-service",
		host_name => "$fqdn",
		service_description => "check_cpu"
	}
	@@nagios_service { "check_open_file_${hostname}":
		check_command => "check_nrpe!check_open_file",
		use => "generic-service",
		host_name => "$fqdn",
		service_description => "check_open_file"
	}
	@@nagios_service { "check_io_${hostname}":
		check_command => "check_nrpe!check_io",
		use => "generic-service",
		host_name => "$fqdn",
		service_description => "check_io"
	}
	@@nagios_service { "check_proc_${hostname}":
		check_command => "check_nrpe!check_proc",
		use => "generic-service",
		host_name => "$fqdn",
		service_description => "check_proc"
	}
	@@nagios_service { "check_net_${hostname}":
		check_command => "check_nrpe!check_net",
		use => "generic-service",
		host_name => "$fqdn",
		service_description => "check_net"
	}
	@@nagios_service { "check_socket_${hostname}":
		check_command => "check_nrpe!check_socket",
		use => "generic-service",
		host_name => "$fqdn",
		service_description => "check_socket"
	}
	@@nagios_service { "check_uptime_${hostname}":
		check_command => "check_nrpe!check_uptime",
		use => "generic-service",
		host_name => "$fqdn",
		service_description => "check_uptime"
	}
}
