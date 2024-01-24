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

## Usage

Building the program from source requires `maven` to be installed.

```sh
## From the root directory (where pom.xml lies)
mvn compile  # compile 

# Run main program (which will simply wait for connection messages)
mvn exec:java -Dexec.mainClass="chatsystem.MainClass" 
```

## What we implemented
When connecting for the first time, you can register using a nickname and a password. These are to be remembered as they are needed for future login.
Your nickname is unique so we check that other users don't have the same nickname as you. We have implemented a partially unique system where we check that our nickname is different from the nickname of other connected users.

Upon registration, a local database is created to store a table of Me and one of Users. In Me, we save our information which will allow us to login in the future. A user is added to the users table if we didn't know them yet with their status set to connected or we update our contact in our database if we already know them.
Each time a user is added to your Users table, a seperate table is created for storing any potential messages exchanged with this user.

Once registered/logged in, you can start exchanging messages with other connected users. On the left side of the ui, is the list of connected contacts to choose from (updated in real time). 
To chat with another user, select their nickname among the online contacts. This colors their nickname in gray and loads the chat history from the database (if any) and allows TCP message exchanging.

If you want to change your nickname, there is a button to do so at the top of the ui, but remember your new nickname as it will be needed for future login. The updated nickname will be communicated and appear on other user's ui as well.
Once you have finished chatting, click the disconnect button. This will properly terminate all tcp sessions you are a part of and inform other users that you are offline (you will no longer appear in their online contacts)

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
