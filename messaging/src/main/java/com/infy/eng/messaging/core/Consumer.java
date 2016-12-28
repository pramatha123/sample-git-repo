package com.infy.eng.messaging.core;

public interface Consumer
{
    public Object receiveMessage();

    public void shutdownConsumer();
}
