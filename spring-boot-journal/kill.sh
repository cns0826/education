
a=`ps -ef | grep java`
b=`echo $a | awk '{ print $2}'`
kill -9 $b
