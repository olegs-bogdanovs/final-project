#!/bin/bash

# Removing and creating dirs
hdfs dfs -rm -r -f /{raw,cleaned,analytic}
hdfs dfs -mkdir -p /{raw/{users,products},cleaned/{users,products},analytic}

#Uploading data into hdfs
hdfs dfs -put users.csv /raw/users
hdfs dfs -put prods.csv /raw/products

#Upload workflows
hdfs dfs -rm -r -f /FinalProject/
hdfs dfs -mkdir -p /FinalProject/lib
hdfs dfs -put workflow.xml /FinalProject/
hdfs dfs -put coordinator.xml /FinalProject/
hdfs dfs -put SparkCleaner-assembly-0.1.jar /FinalProject/lib

