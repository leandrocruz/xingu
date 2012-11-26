
call git pull

call mvn -Declipse.workspace=C:\users\alexandre\dev\eclipse\ws-kidux eclipse:eclipse

REM call mvn -Dmaven.test.failure.ignore=true clean install
call mvn clean install
