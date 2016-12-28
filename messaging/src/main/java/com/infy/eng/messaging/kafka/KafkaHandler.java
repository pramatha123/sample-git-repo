package com.infy.eng.messaging.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.infy.eng.messaging.core.MessageHandler;
import com.infy.eng.messaging.core.MessageException;
import com.infy.eng.messaging.core.MessagePayload;
import com.infy.eng.messaging.kafka.KafkaProperties.KafkaConsumerProperties;
import com.infy.eng.messaging.kafka.KafkaProperties.KafkaProducerProperties;

/**
 * This class helps to handle both the message sending and receiving process through Kafka framework
 * @author Infosys
 *
 */
public class KafkaHandler implements MessageHandler
{
	public final static Logger logger = Logger.getLogger(KafkaHandler.class);
	private static final Map<String, KafkaConsumer> kafkaConsumerMap = new HashMap<String, KafkaConsumer>();
	private static final Map<String, KafkaProducer> kafkaProducerMap = new HashMap<String, KafkaProducer>();
	private static KafkaHandler kafkaHandler;

	/**
	 * Method to send the message to the topic
	 */
	public boolean sendMessage(String topicName, String key, Object message)
	{
		boolean sendSucceeded = false;
		KafkaProducer kafkaProducer;
		try 
		{
			kafkaProducer = kafkaProducerMap.get(topicName);
			if (kafkaProducer == null)
			{
				logger.error("Producer is null, throwing IllegalArgumentException...");
				throw new MessageException("producer.not.found", "Producer should be initialized before sending messages", 3);
			}
			else
			{
				MessagePayload payloadMessage = new MessagePayload(null, message);
				payloadMessage.addMetadata("UUID", String.valueOf(UUID.randomUUID()));
				logger.info("Sending message to topic: " + topicName + " and message: " + payloadMessage.printMessage());
				try
				{
					kafkaProducer.sendMessage(key, payloadMessage);
					sendSucceeded = true;
					logger.info("Message sent to topic: " + topicName);
				}
				catch (Exception e)
				{
					sendSucceeded = false;
					logger.error("Exception when sending message" + e);
				}
			}
		}

		catch (Exception e)
		{
			throw new MessageException("message.not.sent", "Exception occurred while sending message. ", 5);
		}

		return sendSucceeded;
	}

	/**
	 * Method to receive the message in 'MessagePayload' format 
	 */
	@Override
	public MessagePayload receiveMessage(String topicName)
	{
		MessagePayload messagePayload = null;
		try 
		{
			messagePayload =  kafkaConsumerMap.get(topicName).receiveMessage();
		}

		catch (Exception e)
		{
			throw new MessageException("message.not.received", "Exception occurred while receiving message. ", 7);
		}

		return messagePayload;
	}

	public static KafkaHandler getKafkaHandlerInstance()
	{
		if (kafkaHandler == null)
		{
			kafkaHandler = new KafkaHandler();
		}
		return kafkaHandler;
	}

	/**
	 * Method to initialize the producer for Kafka
	 */
	public void initializeProducer(Properties properties)
	{
		try
		{
			String topicName = properties.getProperty(KafkaConstants.TOPIC_NAME);
			String brokerList = properties.getProperty(KafkaConstants.PRODUCER_BROKER_LIST);
			String requestRequiredAcks = properties.getProperty(KafkaConstants.PRODUCER_REQUEST_REQUIRED_ACKS);
			String requestTOutMs = properties.getProperty(KafkaConstants.PRODUCER_REQUEST_TIMEOUT);
			String requestRetryCnt = properties.getProperty(KafkaConstants.PRODUCER_REQUEST_RETRY_COUNT);
			String retryBackoffMs = properties.getProperty(KafkaConstants.PRODUCER_RETRY_BACKOFF);
			String topicMetadataRefreshIntervalMs = properties.getProperty(KafkaConstants.PRODUCER_TOPIC_METADATA_REFRESH_INTERVAL);

			KafkaProducerProperties kafkaProducerProperties = KafkaProperties.producer(topicName, brokerList).requesRequiredAcks(requestRequiredAcks)
					.requestTOutMS(requestTOutMs).maxRequestRetryCnt(requestRetryCnt).retryBackoffMS(retryBackoffMs)
					.topicMetadataRefreshIntervalMs(topicMetadataRefreshIntervalMs).build();

			logger.info("Initializing Kafka producer..");
			topicName = kafkaProducerProperties.getTopicName();
			if (kafkaProducerMap.get(topicName) == null)
			{
				kafkaProducerMap.put(topicName, new KafkaProducer(kafkaProducerProperties));
				logger.info("Initialized Kafka producer to topic: " + topicName);
			}
			else
			{
				logger.error("Initialized already");
			}
		}
		catch (Exception e) 
		{
			throw new MessageException("producer.not.initialized", "Exception occurred while initializing producer. ", 4);
		}
	}

	/**
	 * Method to initialize the consumer for Kafka
	 */
	public void initializeConsumer(Properties properties)
	{

		try 
		{
			String topicName = properties.getProperty(KafkaConstants.TOPIC_NAME);
			String zooKeeperEndPoint = properties.getProperty(KafkaConstants.CONSUMER_ZK_ENDPOINT);
			String zkSessionTOutMS = properties.getProperty(KafkaConstants.CONSUMER_ZK_SESSION_TIMEOUT);
			String zkAutocommitMS = properties.getProperty(KafkaConstants.CONSUMER_ZK_AUTOCOMMIT_TIME);
			String zkSyncTimeMS = properties.getProperty(KafkaConstants.CONSUMER_ZK_SYNC_TIME);
			String consumerGrpName = properties.getProperty(KafkaConstants.CONSUMER_GROUP_NAME);
			int ioThreadCnt = Integer.parseInt(properties.getProperty(KafkaConstants.CONSUMER_IO_THREAD_COUNT));
			int ioThreadSleepTimeMS = Integer.parseInt(properties.getProperty(KafkaConstants.CONSUMER_THREAD_SLEEP_TIME));

			KafkaConsumerProperties kafkaConsumerProperties = KafkaProperties.consumer(topicName, zooKeeperEndPoint).zkSessionTOutMS(zkSessionTOutMS)
					.zkAutoCommitIntervalMS(zkAutocommitMS).zkSyncTimeMS(zkSyncTimeMS).consumerGrpName(consumerGrpName).consumerThreadCnt(ioThreadCnt)
					.consumerThreadSleepTimeMS(ioThreadSleepTimeMS).build();
			logger.info("Consumer properties : " + kafkaConsumerProperties);

			String extTopicName = topicName;

			if (kafkaConsumerMap.get(extTopicName) == null)
			{
				logger.info("Starting initialization of kafka consumer threads for topic: " + topicName);
				ExecutorService consumerThreadExecutorService = Executors.newFixedThreadPool(kafkaConsumerProperties.getConsumerThreadCnt());
				kafkaConsumerMap.put(extTopicName, new KafkaConsumer(kafkaConsumerProperties, consumerThreadExecutorService));
				logger.info("Initialization of kafka consumer threads succeeded");
			}
		}
		catch (Exception e)
		{
			throw new MessageException("consumer.not.initialized", "Exception occurred while initializing consumer. ", 6);
		}
	}
	
	@Override
	public void shutdownProducer()
	{
		Set<Entry<String, KafkaProducer>> kafkaProducerSet = kafkaProducerMap.entrySet();
		for (Entry<String, KafkaProducer> kafkaProducerEntry : kafkaProducerSet)
		{
			kafkaProducerEntry.getValue().shutDownProducer();
		}
	}
	
	public void shutdownConsumer()
	{
		Set<Entry<String, KafkaConsumer>> kafkaConsumerSet = kafkaConsumerMap.entrySet();
		for (Entry<String, KafkaConsumer> kafkaConsumerEntry : kafkaConsumerSet)
		{
			kafkaConsumerEntry.getValue().shutDownConsumerThreads();
		}

	}
}