**************** ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ *****************
		 MESSAGING SYSTEM PROJECT NOTES 
**************** ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ***************** 

^^^^^^^^^^^^^^^^^
Brief Description
^^^^^^^^^^^^^^^^^

This project simplifies messaging integration (i.e sending/receiving messages between applications) using a simple API.
It currently provides implementations using Apache Kafka and Apache ActiveMQ, and can be extended to support
other messaging providers too.

^^^^^^^^^^^^^^^^^
Environment Setup
^^^^^^^^^^^^^^^^^

For ActiveMQ:
------------

1. Download activemq library from http://activemq.apache.org/activemq-5141-release.html
2. Extract the zip file to your own directory.
3. Open command prompt, please traverse to the activemq bin directory. (Example: D:\apache-activemq-5.14.1\bin\win64)
4. Run the command "activemq.bat" to start the ActiveMQ server.
5. Import the project in eclipse as a Maven project.
6. Build the project by specifying maven goals [Example: clean install -U]
7. Run the file "MessageTester.java" as Java application.

For Kafka:
----------

1. Download kafka library from https://www.apache.org/dyn/closer.cgi?path=/kafka/0.10.1.0/kafka-0.10.1.0-src.tgz
2. Extract the zip file to your own directory.
3. Open command prompt, please traverse to the kafka bin directory. (Example: D:\kafka_2.11-0.10.0.0\bin\windows>)
4. Run the command "zookeeper-server-start.bat D:\kafka_2.11-0.10.0.0\config\zookeeper.properties" to start the Zookeeper server.
5. Open another command prompt, please traverse to the kafka bin directory. (Example: D:\kafka_2.11-0.10.0.0\bin\windows>)
6. Run the command "kafka-server-start.bat D:\kafka_2.11-0.10.0.0\config\server.properties" to start the Kafka server.
7. Import the project in eclipse as a Maven project.
8. Build the project by specifying maven goals [Example: clean install -U]
9. Run the file "MessageTester.java" as Java application.

^^^^^^^^^^^^^^^^^^^^^^^
List of Exception codes
^^^^^^^^^^^^^^^^^^^^^^^

1. argument.not.found - Argument / Config entry not found.
2. config.not.loaded - Unable to load the config entries.
3. file.not.found - Error while loading the properties.
4. message.not.sent - Exception occurred while sending message.
5. message.not.received - Exception occurred while receiving message.
6. producer.not.found - Producer should be initialized before sending messages.
7. consumer.not.initialized - Exception occurred while initializing consumer.
8. producer.not.initialized - Exception occurred while initializing producer.

^^^^^^^^^^^^^^
IMPORTANT NOTE
^^^^^^^^^^^^^^

As far as ActiveMQ messaging framework is concerned, we need to initialize the consumer before initializing the producer part.
Because ActiveMQ framework listens to the topic which will be consuming the message only if it's available immediately.