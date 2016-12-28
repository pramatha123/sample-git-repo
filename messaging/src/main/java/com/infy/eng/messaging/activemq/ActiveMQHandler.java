package com.infy.eng.messaging.activemq;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import com.infy.eng.messaging.core.MessageHandler;
import com.infy.eng.messaging.core.MessageException;
import com.infy.eng.messaging.core.MessagePayload;

/**
 * This class helps to handle both the message sending and receiving process through ActiveMQ framework
 * @author Infosys
 *
 */
public final class ActiveMQHandler implements MessageHandler
{
	public final static Logger logger = Logger.getLogger(ActiveMQHandler.class);

	private final Map<String, ActiveMQProducer> producerMap = new HashMap<String, ActiveMQProducer>();
	private final Map<String, ActiveMQConsumer> consumerMap = new HashMap<String, ActiveMQConsumer>();
	private static ActiveMQHandler activeMQHandler;
	ActiveMQProducer producer;
	ActiveMQConsumer consumer;

	private ActiveMQHandler()
	{
		activeMQHandler = this;
	}

	/**
	 * Method to send the message to the topic
	 */
	public boolean sendMessage(String topicName, String key, Object message)
	{
		//ActiveMQProducer producer;
		MessagePayload messagePayload;
		boolean messageSent = false;
		try 
		{
			producer = producerMap.get(topicName);
			messagePayload = new MessagePayload(null, message);
			producer.sendMessage(key, messagePayload);
			logger.info("Message sent successfully to the topic: " + topicName + " for the key: " + key);
			messageSent = true;
		}

		catch (Exception e)
		{
			throw new MessageException("message.not.sent", "Exception occurred while sending message. ", 5);
		}

		return messageSent;
	}

	public Map<String, ActiveMQProducer> getProducerMap()
	{
		return producerMap;
	}

	public static ActiveMQHandler getActiveMQHandlerInstance()
	{
		if (null == activeMQHandler)
		{
			return new ActiveMQHandler();
		}
		return activeMQHandler;
	}

	/**
	 * Method to receive the message in 'MessagePayload' format 
	 */
	public MessagePayload receiveMessage(String topicName)
	{
		MessagePayload messagePayload = null;

		try
		{
			consumer = consumerMap.get(topicName);
			messagePayload = consumer.receiveMessage();
		}

		catch (Exception e)
		{
			throw new MessageException("message.not.received", "Exception occurred while receiving message. ", 6);
		}

		return messagePayload;
	}

	/**
	 * Method to initialize the producer for ActiveMQ
	 */
	public synchronized void initializeProducer(Properties properties)
	{
		ActiveMQProducerProperties activeMQProducerProperties;
		try 
		{
			activeMQProducerProperties = new ActiveMQProducerProperties(properties.getProperty(ActiveMQConstants.CONTEXT_FACTORY), 
					properties.getProperty(ActiveMQConstants.TOPIC_NAME), properties.getProperty(ActiveMQConstants.PRODUCER_BROKER_LIST));
			producerMap.put(activeMQProducerProperties.getTopicName(), new ActiveMQProducer(activeMQProducerProperties));
			activeMQHandler = this;
		}

		catch (Exception e)
		{
			throw new MessageException("producer.not.initialized", "Exception occurred while initializing producer. ", 3);
		}
	}

	/**
	 * Method to initialize the consumer for ActiveMQ
	 */
	public synchronized void initializeConsumer(Properties properties)
	{
		String topicName;

		try 
		{
			ActiveMQConsumerProperties activeMQConsumerProperties = new ActiveMQConsumerProperties(properties.getProperty(ActiveMQConstants.CONTEXT_FACTORY), 
					properties.getProperty(ActiveMQConstants.TOPIC_NAME), properties.getProperty(ActiveMQConstants.PRODUCER_BROKER_LIST), 
					Integer.parseInt(properties.getProperty(ActiveMQConstants.CONSUMER_IO_THREAD_COUNT)));
			topicName = activeMQConsumerProperties.getTopicName();

			if (consumerMap.get(topicName) == null)
			{
				consumerMap.put(topicName, new ActiveMQConsumer(activeMQConsumerProperties));
			}
		}

		catch (Exception e)
		{
			throw new MessageException("consumer.not.initialized", "Exception occurred while initializing consumer. ", 4);
		}
	}

	public void shutdownProducer() 
	{
		producer.shutdownProducer();
	}
	
	public void shutdownConsumer()
	{
		consumer.shutdownConsumer();
	}
}