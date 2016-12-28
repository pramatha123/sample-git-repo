package com.infy.eng.messaging.activemq;

public class ActiveMQConstants
{

    public static final String TOPIC_NAME = "topic.name";
    public static final String PRODUCER_BROKER_LIST = "brokerlist";
    public static final String CONSUMER_IO_THREAD_COUNT = "consumer.iothread.count";
    public static final String CONNECTION_FACTORY_ENDPOINT = "connection.factory";
    public static final String CONTEXT_FACTORY = "context.factory";
    
    public static final String ACTIVEMQ_PROPERTY_FILE_PATH = "/src/main/resources/config/messaging/activemq.properties";
    
    public static final String MESSAGE_CONTENT = "Hello World!!";
    
    public static final String TRUSTED_PACKAGES = "com.infy.eng,com.infy.eng.messaging.core";
    
}