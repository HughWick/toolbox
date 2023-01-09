@echo off 

ECHO "=========RELEASE START===================="
echo cd %~dp0
call mvn clean javadoc:jar deploy -P release -Darguments="gpg.passphrase=xiaozhi10201"
@REM  call mvn clean deploy -P sonatype-oss-release -Darguments="gpg.passphrase=10201"
pause 

