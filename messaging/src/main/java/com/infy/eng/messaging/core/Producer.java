package com.infy.eng.messaging.core;

public interface Producer
{
    public void sendMessage(String key, Object message);

    public void shutDownProducer();
}
