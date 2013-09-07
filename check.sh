s=$(curl -sL -w "%{http_code}" "www.kayankeith.com/health" -o /dev/null)

if [ $s != 200 ] 
then
  /home/ec2-user/kake/restart.sh
fi

s=$(sudo /etc/init.d/mysqld status | grep running | wc -l)

if [ $s != 1 ]
then
  sudo /etc/init.d/mysqld restart
fi