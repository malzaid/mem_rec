#compile java stored procedures
javac -cp "$CLASSPATH:/home/akamzin1/voltdb-ent-6.1/voltdb/*"   PrepareUsers.java


csvloader --separator "," --file movies.csv movies
csvloader --separator "," --file ratings.csv ratings
