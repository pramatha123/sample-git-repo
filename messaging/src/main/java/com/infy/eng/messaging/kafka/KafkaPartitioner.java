package com.infy.eng.messaging.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class KafkaPartitioner implements Partitioner {
	public KafkaPartitioner(VerifiableProperties verifiableProperties) 
	{
	}

	public int partition(Object key, int partitionCnt) 
	{
		return Math.abs(key.hashCode()) % partitionCnt;
	}
}