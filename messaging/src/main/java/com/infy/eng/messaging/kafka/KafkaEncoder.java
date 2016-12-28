package com.infy.eng.messaging.kafka;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

import org.apache.commons.lang.SerializationUtils;

import com.infy.eng.messaging.core.MessagePayload;

public class KafkaEncoder implements Encoder<MessagePayload> 
{
	public KafkaEncoder(VerifiableProperties verifiableProperties) 
	{
	}

	public byte[] toBytes(MessagePayload object) 
	{
		return SerializationUtils.serialize(object);
	}
}