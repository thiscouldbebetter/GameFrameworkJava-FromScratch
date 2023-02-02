#! /bin/sh 

find . -name '*.class' -delete
javac Main/GameFrameworkDemo.java -Xlint:unchecked
java Main/GameFrameworkDemo
