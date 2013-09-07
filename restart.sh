for i in `ps -ef | grep target | grep staged | awk '{print $2}'`
do 
  sudo kill $i
done

rm -rf /home/ec2-user/kake/RUNNING_PID

sudo /home/ec2-user/kake/target/start -Dhttp.port=80 &