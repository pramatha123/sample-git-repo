package com.infy.eng.messaging.kafka;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.apache.log4j.Logger;

import com.infy.eng.messaging.core.MessagePayload;
import com.infy.eng.messaging.kafka.KafkaProperties.KafkaConsumerProperties;

/**
 * This class acts as a consumer to consume the message that has been produced through Kafka framework
 * @author Infosys
 */
public class KafkaConsumer implements com.infy.eng.messaging.core.Consumer
{
	public final static Logger logger = Logger.getLogger(KafkaConsumer.class);
	
    private final ConsumerConnector consumer;
    private final String topic;
    private final int consumerThreadCnt;
    private final ExecutorService consumerThreadES;
    private final Iterator<KafkaStream<byte[], byte[]>> streamIterator;

    /**
     * Constructor to initialize the consumer for Kafka messaging framework
     */
    public KafkaConsumer(KafkaConsumerProperties consumerProperties, ExecutorService consumerThreadExecutorService)
    {
        this.consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(consumerProperties));
        this.topic = consumerProperties.getTopicName();
        this.consumerThreadCnt = consumerProperties.getConsumerThreadCnt();
        this.streamIterator = createStreamIterator();
        this.consumerThreadES = consumerThreadExecutorService;
        logger.info("Consumer initialized successfully");
    }

    /**
     * Create the consumer level config entries 
     * @param consumerProperties
     * @return
     */
    private static ConsumerConfig createConsumerConfig(KafkaConsumerProperties consumerProperties)
    {
        Properties props = new Properties();
        props.put("group.id", consumerProperties.getConsumerGroupName());
        props.put("zookeeper.connect", consumerProperties.getZkEndpnt());
        props.put("zookeeper.session.timeout.ms", consumerProperties.getZkSessionTOutMS());
        props.put("zookeeper.sync.time.ms", consumerProperties.getZkSyncTimeMS());
        props.put("auto.commit.interval.ms", consumerProperties.getZkAutoCommitIntervalMS());
        return new ConsumerConfig(props);
    }

    /**
     * Create an iterator for iterating the Kafka message stream
     * @return
     */
    private Iterator<KafkaStream<byte[], byte[]>> createStreamIterator()
    {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(this.topic, new Integer(this.consumerThreadCnt));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = this.consumer.createMessageStreams(topicCountMap);

        Iterator<KafkaStream<byte[], byte[]>> streamIter = consumerMap.get(this.topic).iterator();
        return streamIter;
    }

    public void shutDownConsumerThreads()
    {
        if (this.consumerThreadES != null && !this.consumerThreadES.isShutdown())
        {
            this.consumerThreadES.shutdown();
        }
    }

    /**
     * Method to receive the message from the topic at client side
     */
    public MessagePayload receiveMessage()
    {

        ConsumerIterator<byte[], byte[]> consumerIterator = null;
        MessagePayload message = null;
        while (streamIterator.hasNext())
        {
            consumerIterator = streamIterator.next().iterator();
        }
        
        while (consumerIterator.hasNext())
        {
            Object messageFromStream = null;
            try
            {
                messageFromStream = parseMessage(consumerIterator.next().message());
                message = (MessagePayload)messageFromStream;
                logger.info("Message received: " + message.getPayload());
                return message;
            }
            catch (Exception e)
            {
                logger.error("Error in while consuming: " + message + "Exception: " + e);
            }
        }
        return message;
    }

    /**
     * Convert message from byte to Object
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
	}
}