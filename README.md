# ChatSystem Project

## Overview
The ChatSystem project aims to create a decentralized chat system for a company, where each employee is assigned their own machine with a fixed IP address. 
All machines in the company are on the same local network.

- **build configuration**: this project can be run and imported based only on the `pom.xml` file.
- **code organization**: code is organized in loosely coupled packages. For instance, the packages `chatsystem.network` and `chatsystem.contacts` are completely independent of each other and only combined in the main class.
- **thread safety**: access to shared resources (e.g. the contact list) is protected by mutex (with the java `synchronized` keyword)
- **tests**: uses `junit` for testing both the network code, the user list code and their combined usage.
- **error handling**: custom error types (e.g. `chatsystem.contacts.ContactAlreadyExists`), and explicit handling of unrecoverable error (error raised higher the chain of responsibility) or recoverable error (handled locally and continue processing)
- **observer design pattern**:
    - implemented by the `UDPServer` class to make it adaptable to different contexts
    - used by the view to have it react to changes in the contact list
- **singleton design pattern**: to ensure that there is a unique instance of the `ContactList` and that is usable across the application
- **logging**: usage of the log4j library (the configuration provided simply prints to the console)
- **continuous integration**: each time the code is pushed to github, the code is compiled and tested (and a notification is sent if that fails). This is enabled by the `.github/worflows/java.yaml` file that sets up Github actions.
- consistent **coding style**: the code here (loosely) adheres to the [Java Coding Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html) that are used by virtually all java programmers.


## Usage

Building the program from source requires `maven` to be installed.

```sh
## From the root directory (where pom.xml lies)
mvn compile  # compile 
mvn test  # run tests

# Run main program (which will simply wait for connection messages)
mvn exec:java -Dexec.mainClass="chatsystem.Main" 
```
In another terminal (linux) you can simulate connection messages:
```sh
echo -n "alice" > /dev/udp/127.0.0.1/1789
echo -n "bob" > /dev/udp/127.0.0.1/1789
echo -n "chloe" > /dev/udp/127.0.0.1/1789
echo -n "alice" > /dev/udp/127.0.0.1/1789
```

### Technologies Used
Java 17
Maven 3.9.5
IntelliJ IDEA

### To connect to another computer from ours
ssh -X identifiant@ordi
This command allows us to access another computer and get their graphics

### Package:
mvn package
This command compiles all classes of the project and produces a single JAR file in the target directory.

### Run:
java -cp target/chatSystem-1.0-SNAPSHOT.jar run.MainClass

### generating a fat jar (jar file that contains all code and all dependencies)
java -jar target/chatSystem-1.0-SNAPSHOT-jar-with-dependencies.jar
