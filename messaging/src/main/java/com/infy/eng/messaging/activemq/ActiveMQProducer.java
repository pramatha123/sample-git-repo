package com.infy.eng.messaging.activemq;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.infy.eng.messaging.core.MessagePayload;

/**
 * This class acts as a producer to produce the message through ActiveMQ framework
 * @author Infosys
 *
 */
public final class ActiveMQProducer
{
	public final static Logger logger = Logger.getLogger(ActiveMQProducer.class);
	
    public ActiveMQHandler activeMQHandler;
    private final Session session;
    private final MessageProducer messageProducer;
    private final String topic;
    private final Properties producerProps;
    private final Connection connection;

    /**
     * Constructor to initialize the producer for ActiveMQ messaging framework
     * @param producerProperties
     */
    public ActiveMQProducer(ActiveMQProducerProperties producerProperties)
    {
        super();
        this.producerProps = populateProducerProps(producerProperties);
        this.topic = producerProperties.getTopicName();
        this.connection = createConnection();
        this.session = createSession();
        this.messageProducer = createMessageProducer();
        logger.info("Producer initialized successfully");
    }

    /**
     * Method to create a connection between ActiveMQ server and producer (client) 
     * @return
     */
    private Connection createConnection()
    {
        InitialContext ctx;
        Connection connection = null;
        try
        {
            ctx = new InitialContext(producerProps);
            ActiveMQConnectionFactory connectionFactory = (ActiveMQConnectionFactory)ctx.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            connection.start();
        }
        catch (NamingException e)
        {
        	logger.error("Error while initializing producer" + e);
        }
        catch (JMSException e)
        {
        	logger.error("Error while initializing producer" + e);
        }

        return connection;
    }

    /**
     * Method to create the producer (client) for the established session
     * @return
     */
    private MessageProducer createMessageProducer()
    {
        MessageProducer producer = null;
        try
        {
            Destination destination = this.session.createTopic(topic);
            producer = this.session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        }
        catch (JMSException e)
        {
        	logger.error("Error while creating producer" + e);
        }

        return producer;

    }

    /**
     * Method to create a session between ActiveMQ server and producer (client) for the established connection
     * @return
     */
    private Session createSession()
    {
        Session session = null;
        try
        {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
        catch (JMSException e)
        {
        	logger.error("Error while creating session" + e);
        }
        return session;
    }

    /**
     * Method to populate the producer properties of ActiveMQ
     * @param producerProperties
     * @return
     */
    private Properties populateProducerProps(ActiveMQProducerProperties producerProperties)
    {
        Properties properties = new Properties();
        properties.put("java.naming.factory.initial", producerProperties.getContextFactory());
        properties.put("java.naming.provider.url", producerProperties.getBrokerList());

        return properties;
    }

    /**
     * Method to send the message to topic from Producer side (client)
     */
    public void sendMessage(String key, Object message)
    {
        ObjectMessage objectMessage;
        try
        {
            objectMessage = session.createObjectMessage((MessagePayload)message);
            this.messageProducer.send(objectMessage);
        }
        catch (JMSException e)
        {
            logger.error("Error while sending message" + e);
        }
    }
    
	public void shutdownProducer()
    {
        try
        {
        	messageProducer.close();
            session.close();
            connection.close();
        }
        catch (JMSException e)
        {
        	logger.error("Error while shutting down producer" + e);
        }
    }
}
