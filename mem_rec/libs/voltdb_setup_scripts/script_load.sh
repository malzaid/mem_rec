#compile java stored procedures

rm procedures/mem_rec/*.class
javac -cp "$CLASSPATH:/home/akamzin1/voltdb-ent-6.1/voltdb/*" procedures/mem_rec/*.java
jar cvf storedprocs.jar procedures/mem_rec/*.class



sqlcmd < ddl.sql
csvloader --separator "," --file movies.csv movies
csvloader --separator "," --file ratings.csv ratings


cd procedures/mem_rec/
rm *.class
javac -cp "$CLASSPATH:/home/akamzin1/voltdb-ent-6.1/voltdb/*" *.java
jar cvf storedprocs.jar *.class
mv storedprocs.jar ../../.
cd ../..