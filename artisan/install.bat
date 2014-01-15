@echo off set loggerConfig=%fp%\log4j2.xml

cd build\jar
java -Dlog4j.configurationFile=%loggerConfig% -jar ArtisanSupport.jar %*
