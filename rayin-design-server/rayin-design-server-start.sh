#!/bin/bash
package_path=/package_publish/rayin
package=rayin-design-server.jar

pid=`ps -ef | grep "$package" | grep -v grep | awk '{print $2}'`
if [ "$pid" != "" ];then
  echo -e "\033[31m already started pid: $pid \033[0m"
  echo -e "\033[31m kill first then start \033[0m"
  kill -9 $pid
else
  echo "no $package process exist"
fi

source /etc/profile
echo -e "\033[32m starting $package \033[0m"
`nohup java -jar $package_path/$package ---spring.profiles.active=dev -Dlog.level=debug -Dlog.root=$package_path/logs -Djava.io.tmpdir=/var/tmp  >/dev/null 2>&1 &`
sleep 3s
pid=`ps -ef | grep "$package" | grep -v grep | awk '{print $2}'`
if [ "$pid" != "" ];then
  echo -e "\033[32m start complete! new pid: $pid \033[0m"
else
  echo -e "\033[31m start failed! please check the jar! \033[0m"
fi
