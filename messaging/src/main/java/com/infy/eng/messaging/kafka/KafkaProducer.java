package com.infy.eng.messaging.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.infy.eng.messaging.core.MessagePayload;
import com.infy.eng.messaging.kafka.KafkaProperties.KafkaProducerProperties;

/**
 * This class acts as a producer to produce the message through Kafka framework
 * @author Infosys
 *
 */
public class KafkaProducer implements com.infy.eng.messaging.core.Producer
{
    private final Producer<String, MessagePayload> producer;
    private final String topic;
    private final Properties producerProps;

    /**
     * Constructor to initialize the Producer for Kafka messaging framework
     * @param producerProperties
     */
    public KafkaProducer(KafkaProducerProperties producerProperties)
    {
        this.producerProps = populateProducerProps(producerProperties);
        this.topic = producerProperties.getTopicName();
        this.producer = new Producer<String, MessagePayload>(new ProducerConfig(producerProps));
    }

    /**
     * Method to populate the producer properties of Kafka
     * @param producerProperties
     * @return
     */
    private Properties populateProducerProps(KafkaProducerProperties producerProperties)
    {
        Properties properties = new Properties();
        properties.put("metadata.broker.list", producerProperties.getBrokerList());
        properties.put("request.required.acks", producerProperties.getRequestRequiredAcks());
        properties.put("request.timeout.ms", producerProperties.getRequestTOutMS());
        properties.put("message.send.max.retries", producerProperties.getMaxRequestRetryCnt());
        properties.put("retry.backoff.ms", producerProperties.getRetryBackoffMS());
        properties.put("topic.metadata.refresh.interval.ms", producerProperties.getTopicMetadataRefreshIntervalMs());
        properties.put("key.serializer.class", KafkaConstants.DEFAULT_SERIALIZER_CLASS);
        properties.put("serializer.class", KafkaConstants.SERIALIZER_CLASS);
        properties.put("partitioner.class", KafkaConstants.PARTITIONER_CLASS);
        
        return properties;
    }
    
    /**
     * Method to send the message to topic from Producer side (client)
     */
    public void sendMessage(String key, Object message) 
    {
    	MessagePayload messagePayload = (MessagePayload) message;
        this.producer.send(new KeyedMessage<String, MessagePayload>(topic, key, messagePayload));
    }

	public void shutDownProducer() 
	{
		producer.close();
	}
}