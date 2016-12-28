package com.infy.eng.messaging.kafka;

import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.infy.eng.messaging.activemq.ActiveMQConstants;
import com.infy.eng.messaging.core.MessageException;
import com.infy.eng.messaging.core.MessageHelper;

/**
 * This class serves as the test class for testing the Kafka messaging system.
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

		try
		{
				String filePath = KafkaConstants.KAFKA_PROPERTY_FILE_PATH; 
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
			KafkaHandler.getKafkaHandlerInstance().initializeProducer(properties);
			KafkaHandler.getKafkaHandlerInstance().initializeConsumer(properties);
			topicName = properties.getProperty(ActiveMQConstants.TOPIC_NAME);
			KafkaHandler.getKafkaHandlerInstance().sendMessage(topicName, UUID.randomUUID().toString(), KafkaConstants.MESSAGE_CONTENT);
			logger.info("Thread wait..");
			Thread.sleep(3000);
			logger.info("Thread wait over..");
			KafkaHandler.getKafkaHandlerInstance().receiveMessage(topicName);
			KafkaHandler.getKafkaHandlerInstance().shutdownProducer();
			KafkaHandler.getKafkaHandlerInstance().shutdownConsumer();
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
