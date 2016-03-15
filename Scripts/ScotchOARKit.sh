##YOU MAY NEED TO RUN THIS SCRIPT UNDER SUDO
mkdir -p ~/SCOTCHOARKIT
cd ~/SCOTCHOARKIT
data=$(curl -g "http://jenkins.zenoton.com/jenkins/job/ScotchOARKit/lastSuccessfulBuild/api/xml?&tree=fingerprint[fileName,hash]&xpath=mavenModuleSetBuild/fingerprint[fileName=%22com.scotch.OARKit:OARKit.jar%22]")
hashRemote=$(sed -ne '/hash/{s/.*<hash>\(.*\)<\/hash>.*/\1/p;q;}' <<< $data)
hashLocal=$(md5 ~/SCOTCHOARKIT/download.jar)
if [[ hashRemote != hashLocal ]]; then
	echo Downloading new Jar
	wget --output-document=download.jar "http://jenkins.zenoton.com/jenkins/job/ScotchOARKit/lastSuccessfulBuild/artifact/target/OARKit.jar/" 
fi
java -jar ~/SCOTCHOARKIT/download.jar server