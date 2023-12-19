
#ChatSystem Project

###Overview
The ChatSystem project aims to create a decentralized chat system for a company, where each employee is assigned their own machine with a fixed IP address. All machines in the company are on the same local network.

###Current Progress
Phase 1: Contact Discovery
In this phase, we have successfully implemented the following features:

Scanning the network and retrieving the ID and nickname of online users.
Communicating chosen pseudonyms to peers.
Maintenance of a list of connected users.

###Technologies Used
Java 17
Maven 3.9.5
IntelliJ IDEA

###Pour aller sur l'autre ordi
ssh -X identifiant@ordi

###Instructions on How to Compile and Run
Compile:
mvn compile
This command compiles all classes in the project.

###Package:
mvn package
This command compiles all classes of the project and produces a single JAR file in the target directory.

###Run:
java -cp target/chatSystem-1.0-SNAPSHOT.jar run.MainClass

###generating a fat jar (jar file that contains all code and all dependencies)
java -jar target/chatSystem-1.0-SNAPSHOT-jar-with-dependencies.jar
