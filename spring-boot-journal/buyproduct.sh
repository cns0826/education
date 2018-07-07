#!/bin/bash

curl -i -XPOST -H "Authorization:Bearer $1" http://35.187.207.139:8080/prod/buy/$2
