#!/bin/bash
curl -i http://localhost:8080/oauth/token -d "grant_type=password&username=superuser&password=sollab123&scope=read" -u admin:kkh4790

