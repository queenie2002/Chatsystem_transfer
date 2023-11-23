#ChatSystem Project

#Overview
Creation of a decentralised chat system where
    - within the company each employee is assigned its own machine, with a fixed IP address
    - all machines of the company are on the same local network

#Current Progress
Finished phase 1 : contact discovery with the following features
   - scanning the network and retrieving the ID and nickname of online users
   - communication of chosen pseudo to peers
   - maintenance of a list of connected users

#Used for the project
    - Java 17
    - Maven 3.9.5
    - IntelliJ IDEA

#Instructions on how to compile and run
    - mvn compile (compiles all classes in the project)
    - mvn package (compiles all classes of the project and produces a single jar file in the target directory)
    - java -cp target/chatSystem-1.0-SNAPSHOT.jar run.MainClass
