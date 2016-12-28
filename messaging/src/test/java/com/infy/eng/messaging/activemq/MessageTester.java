package com.infy.eng.messaging.activemq;

import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.infy.eng.messaging.core.MessageException;
import com.infy.eng.messaging.core.MessageHelper;

/**
 * This class serves as the test class for testing the ActiveMQ messaging system.
 * 
 * @author Infosys
 *
 */
public class MessageTester 
{
	public final static Logger logger = Logger.getLogger(MessageTester.class);

	/**
	 * Main method to initialize the properties of both Producer, Consumer and it allows to send / receive the message to / from the topic 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception
	{
		Properties properties;
		String filePath = ActiveMQConstants.ACTIVEMQ_PROPERTY_FILE_PATH; 
				
		try
		{
				properties = MessageHelper.loadProperties(filePath);
				bindMessage(properties);
				System.exit(0);
		}

		catch (MessageException e)
		{
			logger.error("Exception occurred. Code: " + e.getCode() + " Message: " + e.getErrorMessage());
			System.exit(1);
		}

		catch (Exception e)
		{
			logger.info("Uncategorized exception occurred while processing the message system");
			System.exit(1);
		}
	}

	/**
	 * Method to bind the message between the systems based upon the messaging framework
	 * @param properties
	 * @throws Exception
	 */
	public static void bindMessage(Properties properties) throws Exception
	{
		String topicName;
		
		try 
		{
			//For ActiveMQ framework, initialize the consumer properties before initializing producer
			ActiveMQHandler.getActiveMQHandlerInstance().initializeConsumer(properties);
			ActiveMQHandler.getActiveMQHandlerInstance().initializeProducer(properties);
			topicName = properties.getProperty(ActiveMQConstants.TOPIC_NAME);
			ActiveMQHandler.getActiveMQHandlerInstance().sendMessage(topicName, UUID.randomUUID().toString(), ActiveMQConstants.MESSAGE_CONTENT);
			logger.info("Thread wait..");
			Thread.sleep(3000);
			logger.info("Thread wait over..");
			ActiveMQHandler.getActiveMQHandlerInstance().receiveMessage(topicName);
			ActiveMQHandler.getActiveMQHandlerInstance().shutdownProducer();
			ActiveMQHandler.getActiveMQHandlerInstance().shutdownConsumer();
		}

		catch (MessageException e)
		{
			logger.error("Exception occurred. Code: " + e.getCode() + " Message: " + e.getErrorMessage());
			System.exit(1);
		}

		catch (Exception e)
		{
			logger.info("Uncategorized exception occurred while processing the message system");
			System.exit(1);
		}
	}
}
