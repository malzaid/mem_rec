#!/bin/bash

#compile java stored procedures


cd procedures/mem_rec/
rm *.class
javac -cp "$CLASSPATH:/home/akamzin1/voltdb-ent-6.1/voltdb/*" *.java
jar cvf storedprocs.jar *.class
mv storedprocs.jar ../../.
cd ../..
sqlcmd < ddl.sql
csvloader --separator "," --file movies.csv movies_100k
csvloader --separator "," --file ratings.csv ratings_100k

csvloader --separator ";" --file movies.csv movies_1m
csvloader --separator ";" --file ratings.csv ratings_1m

csvloader --separator "," --file movies.csv movies_20m
csvloader --separator "," --file ratings.csv ratings_20m

