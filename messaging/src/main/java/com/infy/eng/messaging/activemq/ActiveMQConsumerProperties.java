package com.infy.eng.messaging.activemq;

import org.apache.log4j.Logger;

/**
 * This class helps to load the consumer level properties of ActiveMQ framework
 * @author Infosys
 *
 */
public class ActiveMQConsumerProperties
{
	public final static Logger logger = Logger.getLogger(ActiveMQConsumerProperties.class);
	
    private final String contextFactory;
    private final String brokerList;
    private final String topicName;
    private final int threadCount;

    /**
     * Constructor to load the consumer level properties for ActiveMQ
     */
    public ActiveMQConsumerProperties(String contextFactory, String topicName, String brokerList, int threadCount)
    {
        super();
        this.contextFactory = contextFactory;
        this.topicName = topicName;
        this.brokerList = brokerList;
        this.threadCount = threadCount;
        logger.info("Consumer properties loaded successfully");
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

   

    //logger added
}
