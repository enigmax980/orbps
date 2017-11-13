
echo node version :  && node -v && echo npm version : && call npm -version
cd %~d0
cd %~dp0 
if errorlevel 0 (

	if not exist .\node_modules\ (
		echo front web env prepare
		call npm --loglevel info install
		echo front web env is ready
	) 

	echo front web code build begin
	.\node_modules\.bin\gulp build
	echo front web code build end

) else (
	echo please install nodejs!!!
	
)



