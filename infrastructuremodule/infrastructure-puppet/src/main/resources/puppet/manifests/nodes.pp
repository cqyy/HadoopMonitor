node 'centos' {
include nagios::monitor
include nagios::nagios-command
include nagios-api::server
include nagios::target
include nagios::nrpe
}
node 'default' {
include nagios::target
include nagios::nrpe
}
