#!/bin/bash
echo node version :  && node -v && echo npm version : &&  npm -version
if [ $? -eq 0 ]
then
	if [ ! -d ./node_modules/.bin ]
	then 
		echo front web env prepare
		npm install
		echo front web env is ready
	fi
	echo front web code build begin
	./node_modules/.bin/gulp build
	echo front web code build end
else
	echo please install nodejs!!!
fi
