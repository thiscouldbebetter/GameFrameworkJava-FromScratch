#! /bin/sh
javac -sourcepath . GameFramework/GameFramework.java -Xmaxerrs 10000 2>Errors.txt
cat Errors.txt
