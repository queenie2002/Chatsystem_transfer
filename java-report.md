# CHATSYSTEM JAVA REPORT

## Reasons behind the choices in our technology and potential criticism :

### UDP (User Datagram Protocol):
Motivation: Ideal for broadcasting status updates due to low overhead and fast delivery. Suitable for time-sensitive, non-critical information.
Criticism: Lacks reliability, message delivery and order are not guaranteed, which can lead to inconsistent updates across the network.

### TCP (Transmission Control Protocol):
Motivation: Ensures reliable and ordered delivery of chat messages, crucial for coherent and consistent communication.
Criticism: Higher latency due to connection setup, acknowledgments, and congestion control, potentially slowing down message exchanges.

### SQLite3:
Motivation: Lightweight, file-based database, perfect for local storage of user data and message history without a server process.
Criticism: Limited scalability and concurrency support, not ideal for larger applications with high write demands.

### Swing (Java):
Motivation: Provides a range of customizable UI components, fully integrated with Java, for a responsive chat interface.
Criticism: Can appear outdated and lacks modern features compared to newer UI frameworks like JavaFX.



## Testing policy : 

Unit testing was used to ensure the reliability of individual components, focusing on the functionality of methods and classes. 
With a method assertThrows that returns true if an exception was raised, exceptions being properly thrown can be checked.
To handle exceptions that are not meant to happen, the exception is added to the method’s signature so it fails the test.
We use assert to check booleans or values using comparing tools and created some functions to test the functionality of some methods (ex: numberTablesInDatabase that returns the number of tables in the database to check if the tables were properly created).
Furthermore, to have accurate tests, we manage the test data and reset before every test with @BeforeEach. For example, in ContactListTests, we delete then start the connection for the database of the users and clear the contact list to start from the same base.

Manual testing of the system was used to simulate real user scenarios, to check the proper working of the user interface, controller that responds to certain observers and the database.  For the network package, we worked with ssh to simulate being on different computers. After every new method, we would test it by printing useful information, checking the database in the terminal and observing the view.  

Additionally, automated testing on unit tests was integrated into the GitHub workflow, enabling automatic test execution upon code push, thus ensuring code quality and stability with every update. 





