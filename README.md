# Message broker  

###Java application of message broker pattern.

####Technologies: Java SE 1.8 (Threads, Sockets) + Apache Maven.

####Possible actions: 

1. Add topic

2. Post statue to the topic

3. Subscribe to the topic

4. Get all the topics.

Main class - Server. 

####Usage:

Create Server class object. 

You can connect to it by sockets. Send action (1-4) to it, that:

1. Send name of the topic

2. Send name of the topic and text of the statue.

3. Send name of the topic and port of your socket (default host is "localhost")

4. Send port of your socket. Then get number of topics and them one by one

At the end send -1 to end the communication.

###Instalstion

Run mvn clean install

### Task for yandex internship. (Task 2 - Plans for future.txt)
