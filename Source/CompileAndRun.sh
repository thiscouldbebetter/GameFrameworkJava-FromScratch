#! /bin/sh 

find . -name '*.class' -delete
javac Main/GameFrameworkDemo.java
java Main/GameFrameworkDemo
