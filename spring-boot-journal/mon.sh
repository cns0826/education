#!/bin/bash
NOW=`date +'%Y%m%d_%H%m%S'`
echo "-----------------START----------------"
echo $NOW

while true
do
DATE=`date +'%m%d_%H%M%S'`
result1=`netstat -anp | grep 13.125.130.142`
result2=`netstat -anp | grep 8080`
echo $DATE $result1
echo $DATE $result2
sleep 1
done

