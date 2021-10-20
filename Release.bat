@echo off 

ECHO "=========RELEASE START===================="
D:
cd D:\private\toolbox
call mvn clean javadoc:jar deploy -P release
@REM  call mvn clean deploy -P sonatype-oss-release -Darguments="gpg.passphrase=10201"
pause 

