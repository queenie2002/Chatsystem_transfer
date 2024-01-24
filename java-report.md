Reasons behind the choices in our technology and potential criticism :

UDP (User Datagram Protocol):
Motivation: Ideal for broadcasting status updates due to low overhead and fast delivery. Suitable for time-sensitive, non-critical information.
Criticism: Lacks reliability, message delivery and order are not guaranteed, which can lead to inconsistent updates across the network.

TCP (Transmission Control Protocol):
Motivation: Ensures reliable and ordered delivery of chat messages, crucial for coherent and consistent communication.
Criticism: Higher latency due to connection setup, acknowledgments, and congestion control, potentially slowing down message exchanges.

SQLite3:
Motivation: Lightweight, file-based database, perfect for local storage of user data and message history without a server process.
Criticism: Limited scalability and concurrency support, not ideal for larger applications with high write demands.

Swing (Java):
Motivation: Provides a range of customizable UI components, fully integrated with Java, for a responsive chat interface.
Criticism: Can appear outdated and lacks modern features compared to newer UI frameworks like JavaFX.



Testing policy
Initially, unit testing was used to ensure the reliability of individual components, focusing on the functionality of methods and classes. 
Then, manual testing of the system was used to simulate real user scenarios, to check the proper working of the user interface and databases. 
Additionally, automated testing was integrated into the GitHub workflow, enabling automatic test execution upon code commits or pull requests, thus ensuring code quality and stability with every update. 