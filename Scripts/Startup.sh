#!/bin/sh
### BEGIN INIT INFO
# Provides:          OARKIT
# Required-Start:    $local_fs $network $named $time $syslog
# Required-Stop:     $local_fs $network $named $time $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Description:       Running the Server
### END INIT INFO

SCRIPT='
##YOU MAY NEED TO RUN THIS SCRIPT UNDER SUDO
mkdir -p ~/SCOTCHOARKIT
cd ~/SCOTCHOARKIT
data=$(curl -g "http://jenkins.zenoton.com/jenkins/job/ScotchOARKit/lastSuccessfulBuild/api/xml?&tree=fingerprint[fileName,hash]&xpath=mavenModuleSetBuild/fingerprint[fileName=%22com.scotch.OARKit:OARKit.jar%22]")
hashRemote=$(sed -ne "/hash/{s/.*<hash>\(.*\)<\/hash>.*/\1/p;q;}" <<< $data)
hashLocal=$(md5 ~/SCOTCHOARKIT/download.jar)
if [[ hashRemote != hashLocal ]]; then
  echo Downloading new Jar
  wget --output-document=download.jar "http://jenkins.zenoton.com/jenkins/job/ScotchOARKit/lastSuccessfulBuild/artifact/target/OARKit.jar/" 
fi
java -jar ~/SCOTCHOARKIT/download.jar server
'
RUNAS=root

PIDFILE=/var/run/OARKIT.pid
LOGFILE=/var/log/OARKIT.log

start() {
  if [ -f /var/run/$PIDNAME ] && kill -0 $(cat /var/run/$PIDNAME); then
    echo 'Service already running' >&2
    return 1
  fi
  echo 'Starting service…' >&2
  local CMD="$SCRIPT &> \"$LOGFILE\" & echo \$!"
  su -c "$CMD" $RUNAS > "$PIDFILE"
  echo 'Service started' >&2
}

stop() {
  if [ ! -f "$PIDFILE" ] || ! kill -0 $(cat "$PIDFILE"); then
    echo 'Service not running' >&2
    return 1
  fi
  echo 'Stopping service…' >&2
  kill -15 $(cat "$PIDFILE") && rm -f "$PIDFILE"
  echo 'Service stopped' >&2
}

uninstall() {
  echo -n "Are you really sure you want to uninstall this service? That cannot be undone. [yes|No] "
  local SURE
  read SURE
  if [ "$SURE" = "yes" ]; then
    stop
    rm -f "$PIDFILE"
    echo "Notice: log file is not be removed: '$LOGFILE'" >&2
    update-rc.d -f <NAME> remove
    rm -fv "$0"
  fi
}

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  uninstall)
    uninstall
    ;;
  retart)
    stop
    start
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|uninstall}"
esac