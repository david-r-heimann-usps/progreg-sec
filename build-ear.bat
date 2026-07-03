set "PROFILE=%~1"
if "%PROFILE%"=="" set "PROFILE=websphere"
SET PRMAIN=c:\Users\tkwtd0\pr
set PATH=%PRMAIN%\apache-maven-3.9.9\bin;%PRMAIN%\jdk-17\bin;%PRMAIN%\nodejs;%PATH%
set JAVA_HOME=%PRMAIN%\jdk1.8.0
set npm_config_cachex=%PRMAIN%\npmcache
set npm_config_proxy=http://56.0.83.24:8080/
mvn clean install -P%PROFILE%