package com.infy.eng.messaging.core;

import java.util.Properties;

public interface MessageHandler
{
    public void initializeProducer(Properties properties);
    
    public void initializeConsumer(Properties properties);

    public boolean sendMessage(String topicName, String key, Object message);

    public MessagePayload receiveMessage(String topicName);
    
    public void shutdownProducer();

    public void shutdownConsumer();
}
