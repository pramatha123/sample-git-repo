package com.infy.eng.messaging.activemq;

/**
 * This class helps to load the producer level properties of ActiveMQ framework
 * @author Infosys
 *
 */
public final class ActiveMQProducerProperties
{
    private final String contextFactory;
    private final String brokerList;
    private final String topicName;

    public ActiveMQProducerProperties(String contextFactory, String topicName, String brokerList)
    {
        super();
        this.contextFactory = contextFactory;
        this.topicName = topicName;
        this.brokerList = brokerList;
    }

    public String getTopicName()
    {
        return topicName;
    }

    public String getBrokerList()
    {
        return brokerList;
    }

    public String getContextFactory()
    {
        return contextFactory;
    }

}