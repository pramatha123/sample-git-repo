package com.infy.eng.messaging.kafka;

public class KafkaConstants
{

    public static final String TOPIC_NAME = "topic.name";
    public static final String PRODUCER_BROKER_LIST = "brokerlist";
    public static final String PRODUCER_REQUEST_REQUIRED_ACKS = "producer.request.required.acks";
    public static final String PRODUCER_REQUEST_TIMEOUT = "producer.request.timeout.ms";
    public static final String PRODUCER_REQUEST_RETRY_COUNT = "producer.request.retry.count";
    public static final String PRODUCER_RETRY_BACKOFF = "producer.retry.backoff.ms";
    public static final String PRODUCER_TOPIC_METADATA_REFRESH_INTERVAL = "producer.topicmetadatarefreshinterval.ms";
    public static final String CONSUMER_ZK_ENDPOINT = "consumer.zk.endpoint";
    public static final String CONSUMER_ZK_SESSION_TIMEOUT = "consumer.zk.session.tout.ms";
    public static final String CONSUMER_ZK_AUTOCOMMIT_TIME = "consumer.zk.autocommit.ms";
    public static final String CONSUMER_ZK_SYNC_TIME = "consumer.zk.synctime.ms";
    public static final String CONSUMER_GROUP_NAME = "consumer.groupname";
    public static final String CONSUMER_IO_THREAD_COUNT = "consumer.iothread.count";
    public static final String CONSUMER_THREAD_SLEEP_TIME = "consumer.sleeptime.ms";
    
    public static final String KAFKA_PROPERTY_FILE_PATH = "/src/main/resources/config/messaging/kafka.properties";
    
    public static final String MESSAGE_SYSTEM_KAFKA = "kafka";
    
    public static final String MESSAGE_CONTENT = "Hello World!!";
    
    public static final String DEFAULT_SERIALIZER_CLASS = "kafka.serializer.StringEncoder";
    public static final String SERIALIZER_CLASS = "com.infy.eng.messaging.kafka.KafkaEncoder";
    public static final String PARTITIONER_CLASS = "com.infy.eng.messaging.kafka.KafkaPartitioner";
}