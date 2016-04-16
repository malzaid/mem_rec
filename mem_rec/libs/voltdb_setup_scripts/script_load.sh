#!/bin/bash

#compile java stored procedures


cd procedures/mem_rec/
rm *.class
javac -cp "$CLASSPATH:/home/akamzin1/voltdb-ent-6.1/voltdb/*" *.java
jar cvf storedprocs.jar *.class
mv storedprocs.jar ../../.
cd ../..
sqlcmd < ddl.sql
csvloader --separator "," --file movies.csv movies
csvloader --separator "," --file ratings.csv ratings
