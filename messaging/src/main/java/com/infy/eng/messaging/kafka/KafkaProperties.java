package com.infy.eng.messaging.kafka;

import com.infy.eng.messaging.core.MessagePayload;

public class KafkaProperties
{
    public static KafkaProducerBuilder producer(String topicName, String brokerList)
    {
        return new KafkaProducerBuilder(topicName, brokerList);
    }

    public static KafkaConsumerBuilder consumer(String topicName, String zkEndpnt)
    {
        return new KafkaConsumerBuilder(topicName, zkEndpnt, null);
    }

    public static KafkaConsumerBuilder consumer(String topicName, String zkEndpnt,String zkEndPointDC)
    {
        return new KafkaConsumerBuilder(topicName, zkEndpnt, zkEndPointDC);
    }

    public static final class KafkaProducerProperties
    {
        private final String topicName;
        private final String brokerList;
        private final String requestRequiredAcks;
        private final String requestTOutMS;
        private final String maxRequestRetryCnt;
        private final String retryBackoffMS;
        private final String topicMetadataRefreshIntervalMs;

        public KafkaProducerProperties(KafkaProducerBuilder producerBuilder)
        {
            this.topicName = producerBuilder.topicName;
            this.brokerList = producerBuilder.brokerList;
            this.requestRequiredAcks = producerBuilder.requestRequiredAcks;
            this.requestTOutMS = producerBuilder.requestTOutMS;
            this.maxRequestRetryCnt = producerBuilder.maxRequestRetryCnt;
            this.retryBackoffMS = producerBuilder.retryBackoffMS;
            this.topicMetadataRefreshIntervalMs = producerBuilder.topicMetadataRefreshIntervalMs;
        }

        public String getTopicName()
        {
            return topicName;
        }

        public String getBrokerList()
        {
            return brokerList;
        }

        public String getRequestRequiredAcks()
        {
            return requestRequiredAcks;
        }

        public String getRequestTOutMS()
        {
            return requestTOutMS;
        }

        public String getMaxRequestRetryCnt()
        {
            return maxRequestRetryCnt;
        }

        public String getRetryBackoffMS()
        {
            return retryBackoffMS;
        }

        public String getTopicMetadataRefreshIntervalMs()
        {
            return topicMetadataRefreshIntervalMs;
        }
    }

    public static final class KafkaProducerBuilder
    {
        private String topicName;
        private String brokerList;
        private String requestRequiredAcks;
        private String requestTOutMS;
        private String maxRequestRetryCnt;
        private String retryBackoffMS;
        private String topicMetadataRefreshIntervalMs;

        private KafkaProducerBuilder(String topicName, String brokerList)
        {
            this.topicName = topicName;
            this.brokerList = brokerList;
            this.requestRequiredAcks = "0";
            this.requestTOutMS = "1000";
            this.maxRequestRetryCnt = "1";
            this.retryBackoffMS = "100";
            this.topicMetadataRefreshIntervalMs = "600000";
        }

        public KafkaProducerBuilder requesRequiredAcks(String requestRequiredAcks)
        {
            this.requestRequiredAcks = requestRequiredAcks;
            return this;
        }

        public KafkaProducerBuilder requestTOutMS(String requestTOutMS)
        {
            this.requestTOutMS = requestTOutMS;
            return this;
        }

        public KafkaProducerBuilder maxRequestRetryCnt(String maxRequestRetryCnt)
        {
            this.maxRequestRetryCnt = maxRequestRetryCnt;
            return this;
        }

        public KafkaProducerBuilder retryBackoffMS(String retryBackoffMS)
        {
            this.retryBackoffMS = retryBackoffMS;
            return this;
        }

        public KafkaProducerBuilder topicMetadataRefreshIntervalMs(String topicMetadataRefreshIntervalMs)
        {
            this.topicMetadataRefreshIntervalMs = topicMetadataRefreshIntervalMs;
            return this;
        }

        public KafkaProducerProperties build()
        {
            return (new KafkaProducerProperties(this));
        }
    }

    public static final class KafkaConsumerProperties
    {
        private final String topicName;
        private final String zkEndpointDC;
        private final String consumerThreadNamePrefix;
        private final int consumerThreadCnt;
        private final int consumerThreadSleepTimeMS;
        private final int consumerThreadRetrySleepTimeMS;
        private final int taskQueueCapacity;
        private final String consumerGroupName;
        private final String zkEndpnt;
        private final String zkSessionTOutMS;
        private final String zkSyncTimeMS;
        private final String zkAutoCommitIntervalMS;
        private final Class<?> messageTypeClass;

        private KafkaConsumerProperties(KafkaConsumerBuilder consumerBuilder)
        {
            this.topicName = consumerBuilder.topicName;
            this.zkEndpointDC = consumerBuilder.zkEndpointDC;
            this.zkEndpnt = consumerBuilder.zkEndpnt;
            this.consumerThreadNamePrefix = consumerBuilder.consumerThreadNamePrefix;
            this.consumerThreadCnt = consumerBuilder.consumerThreadCnt;
            this.consumerThreadSleepTimeMS = consumerBuilder.consumerThreadSleepTimeMS;
            this.consumerThreadRetrySleepTimeMS = consumerBuilder.consumerThreadRetrySleepTimeMS;
            this.taskQueueCapacity = consumerBuilder.taskQueueCapacity;
            this.consumerGroupName = consumerBuilder.consumerGroupName;
            this.zkSessionTOutMS = consumerBuilder.zkSessionTOutMS;
            this.zkSyncTimeMS = consumerBuilder.zkSyncTimeMS;
            this.zkAutoCommitIntervalMS = consumerBuilder.zkAutoCommitIntervalMS;
            this.messageTypeClass = consumerBuilder.messageTypeClass;
        }

        public String getTopicName()
        {
            return topicName;
        }

        public String getZkEndpointDC()
        {
            return zkEndpointDC;
        }

        public String getConsumerThreadNamePrefix()
        {
            return consumerThreadNamePrefix;
        }

        public int getConsumerThreadCnt()
        {
            return consumerThreadCnt;
        }

        public int getConsumerThreadSleepTimeMS()
        {
            return consumerThreadSleepTimeMS;
        }

        public int getConsumerThreadRetrySleepTimeMS()
        {
            return consumerThreadRetrySleepTimeMS;
        }

        public int getTaskQueueCapacity()
        {
            return taskQueueCapacity;
        }

        public String getConsumerGroupName()
        {
            return consumerGroupName;
        }

        public String getZkEndpnt()
        {
            return zkEndpnt;
        }

        public String getZkSessionTOutMS()
        {
            return zkSessionTOutMS;
        }

        public String getZkSyncTimeMS()
        {
            return zkSyncTimeMS;
        }

        public String getZkAutoCommitIntervalMS()
        {
            return zkAutoCommitIntervalMS;
        }

        public Class<?> getMessageTypeClass()
        {
            return messageTypeClass;
        }
    }

    public static final class KafkaConsumerBuilder
    {
        private String topicName;
        private String zkEndpointDC;
        private String consumerThreadNamePrefix;
        private int consumerThreadCnt;
        private int consumerThreadSleepTimeMS;
        private int consumerThreadRetrySleepTimeMS;
        private int taskQueueCapacity;
        private String consumerGroupName;
        private String zkEndpnt;
        private String zkSessionTOutMS;
        private String zkSyncTimeMS;
        private String zkAutoCommitIntervalMS;
        private Class<?> messageTypeClass;

        private KafkaConsumerBuilder(String topicName, String zkEndpnt, String zkEndpointDC)
        {
            this.topicName = topicName;
            this.zkEndpointDC = zkEndpointDC;
            this.zkEndpnt = zkEndpnt;
            this.consumerThreadNamePrefix = "Consumer_Thread_";
            this.consumerThreadCnt = 1;
            this.consumerThreadSleepTimeMS = 1000;
            this.consumerThreadRetrySleepTimeMS = 100;
            this.taskQueueCapacity = 1024;
            this.consumerGroupName = "Default_Kafka_Client";
            this.zkSessionTOutMS = "6000";
            this.zkSyncTimeMS = "2000";
            this.zkAutoCommitIntervalMS = "1000";
            this.messageTypeClass = MessagePayload.class;
        }

        public KafkaConsumerBuilder consumerThreadNamePrefix(String consumerThreadNamePrefix)
        {
            this.consumerThreadNamePrefix = consumerThreadNamePrefix;
            return this;
        }

        public KafkaConsumerBuilder consumerThreadCnt(int consumerThreadCnt)
        {
            this.consumerThreadCnt = consumerThreadCnt;
            return this;
        }

        public KafkaConsumerBuilder consumerThreadSleepTimeMS(int consumerThreadSleepTimeMS)
        {
            this.consumerThreadSleepTimeMS = consumerThreadSleepTimeMS;
            return this;
        }

        public KafkaConsumerBuilder consumerThreadRetrySleepTimeMS(int consumerThreadRetrySleepTimeMS)
        {
            this.consumerThreadRetrySleepTimeMS = consumerThreadRetrySleepTimeMS;
            return this;
        }

        public KafkaConsumerBuilder taskQueueCapacity(int taskQueueCapacity)
        {
            this.taskQueueCapacity = taskQueueCapacity;
            return this;
        }

        public KafkaConsumerBuilder consumerGrpName(String consumerGroupName)
        {
            this.consumerGroupName = consumerGroupName;
            return this;
        }

        public KafkaConsumerBuilder zkSessionTOutMS(String zkSessionTOutMS)
        {
            this.zkSessionTOutMS = zkSessionTOutMS;
            return this;
        }

        public KafkaConsumerBuilder zkSyncTimeMS(String zkSyncTimeMS)
        {
            this.zkSyncTimeMS = zkSyncTimeMS;
            return this;
        }

        public KafkaConsumerBuilder zkAutoCommitIntervalMS(String zkAutoCommitIntervalMS)
        {
            this.zkAutoCommitIntervalMS = zkAutoCommitIntervalMS;
            return this;
        }

        public KafkaConsumerBuilder messageTypeClass(Class<?> messageTypeClass)
        {
            this.messageTypeClass = messageTypeClass;
            return this;
        }

        public KafkaConsumerProperties build()
        {
            return (new KafkaConsumerProperties(this));
        }
    }
}