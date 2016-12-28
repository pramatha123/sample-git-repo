package com.infy.eng.messaging.activemq;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;

import com.infy.eng.messaging.core.Consumer;
import com.infy.eng.messaging.core.MessagePayload;

/**
 * This class acts as a consumer to consume the message that has been produced through ActiveMQ framework
 * @author Infosys
 *
 */
public class ActiveMQConsumer implements Consumer, ExceptionListener
{
	public final static Logger logger = Logger.getLogger(ActiveMQConsumer.class);
	
    private final Properties consumerProps;
    private final String topic;
    private final int consumerThreadCnt;
    private final Connection connection;
    private final Session session;
    private final MessageConsumer messageConsumer;
    
    /**
     * Constructor to initialize the consumer for ActiveMQ messaging framework
     * @param consumerProperties
     */
    public ActiveMQConsumer(ActiveMQConsumerProperties consumerProperties)
    {
        this.consumerProps = populateConsumerProps(consumerProperties);
        this.topic = consumerProperties.getTopicName();
        this.consumerThreadCnt = consumerProperties.getThreadCount();
        this.connection = createConnection();
        this.session = createSession();
        this.messageConsumer = createMessageConsumer();
        logger.info("Consumer initialized successfully");
    }
    
    /**
     * Method to create a connection between ActiveMQ server and consumer (client) 
     * @return
     */
    private Connection createConnection()
    {
        InitialContext ctx;
        Connection connection = null;
        try
        {
            ctx = new InitialContext(consumerProps);
            ActiveMQConnectionFactory connectionFactory = (ActiveMQConnectionFactory)ctx.lookup("ConnectionFactory");
            connectionFactory.setTrustedPackages(new ArrayList<String>(Arrays.asList(ActiveMQConstants.TRUSTED_PACKAGES.split(","))));
            connection = connectionFactory.createConnection();
            connection.start();
            connection.setExceptionListener(this);
        }
        catch (NamingException e)
        {
        	logger.error("Error while initializing consumer" + e);
        }
        catch (JMSException e)
        {
        	logger.error("Error while initializing consumer" + e);
        }

        return connection;
    }

    /**
     * Method to populate the consumer properties of ActiveMQ
     * @param consumerProperties
     * @return
     */
    private Properties populateConsumerProps(ActiveMQConsumerProperties consumerProperties)
    {
        Properties properties = new Properties();
        properties.put("java.naming.factory.initial", consumerProperties.getContextFactory());
        properties.put("java.naming.provider.url", consumerProperties.getBrokerList());

        return properties;
    }

    /**
     * Method to create a session between ActiveMQ server and consumer (client) for the established connection 
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
        	logger.error("Error while creating session in consumer" + e);
        }
        return session;
    }

    /**
     * Method to create the consumer (client) for the established session
     * @return
     */
    private MessageConsumer createMessageConsumer()
    {
        Destination destination;
        MessageConsumer consumer = null;
        try
        {
            destination = session.createTopic(topic);
            consumer = session.createConsumer(destination);
        }
        catch (JMSException e)
        {
        	logger.error("Error while creating consumer" + e);
        }

        return consumer;

    }

    @Override
    public synchronized void onException(JMSException arg0)
    {
    	
    }

    /**
     * Method to receive the message from the topic at client side
     */
    @Override
    public MessagePayload receiveMessage()
    {
        Message message = null;
        MessagePayload messagePayload = null;
        ActiveMQObjectMessage objectMessage;
        Object messageFromStream = null;
        try
        {
            message = messageConsumer.receive(1000);
            
            if (message instanceof ActiveMQObjectMessage)
            {
            	objectMessage = (ActiveMQObjectMessage)message;
                messageFromStream = parseMessage(objectMessage.getContent().getData());
                messagePayload = (MessagePayload)messageFromStream;
                logger.info("Message received: " + messagePayload.getPayload());
            }
            else
            {
            	logger.info("Message received: " + message);
            }
        }
        catch (JMSException e)
        {
        	logger.error("Error while receiving the message at consumer end" + e);
        }
        
        return messagePayload;
    }
	
	 /**
     * Method to parse the message from byte to Object
     * @param data
     * @return
     */
    private Object parseMessage(byte[] data)
	{
		Object kafkaStreamMessage = null;
		InputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectInputStream;
		try
		{
			objectInputStream = new ObjectInputStream(inputStream);
			kafkaStreamMessage = objectInputStream.readObject();
		}
		catch (IOException e)
		{
			logger.error("Parsing error occurred. " + e);
		}
		catch (ClassNotFoundException e)
		{
			logger.error("Parsing error occurred. " + e);
		}
		return kafkaStreamMessage;
	}
    
    @Override
    public void shutdownConsumer()
    {
        try
        {
            messageConsumer.close();
            session.close();
            connection.close();
        }
        catch (JMSException e)
        {
        	logger.error("Error while shutting down consumer" + e);
        }
    }
}
