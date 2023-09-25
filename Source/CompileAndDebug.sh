#! /bin/sh 

find . -name '*.class' -delete
javac Main/GameFrameworkDemo.java -Xlint:unchecked
jdb Main/GameFrameworkDemo
