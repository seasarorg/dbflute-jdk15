@echo off

set ANT_OPTS=-Xmx512M

set DBFLUTE_HOME=..\mydbflute\dbflute-0.9.9.5B

set MY_PROJECT_NAME=exampledb

set MY_PROPERTIES_PATH=build.properties

if "%pause_at_end%"=="" set pause_at_end=y
