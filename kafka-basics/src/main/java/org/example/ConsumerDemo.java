package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConsumerDemo {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerDemo.class);

    public static void main(String[] args) {

        logger.info("This is ConsumerDemo");

        final String topic = "first_topic";
        final String groupId = "test2_group";

        // create consumer properties
        Properties properties = new Properties();

        // connect to Kafka Server
        properties.setProperty(KafkaKeys.BOOTSTRAP_SERVERS, "localhost:9092");

        // set producer properties
        properties.setProperty(KafkaKeys.KEY_DESERIALIZER, StringDeserializer.class.getName());
        properties.setProperty(KafkaKeys.VALUE_DESERIALIZER, StringDeserializer.class.getName());

        properties.setProperty(KafkaKeys.GROUP_ID, groupId);
        properties.setProperty(KafkaKeys.AUTO_OFFSET_RESET, "latest");

        // create the consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // subscribe to a topic
        consumer.subscribe(List.of(topic));

        // poll for data
        while (true) {

            logger.info("polling");

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord record : records) {

                logger.info("Key: " + record.key() + ", Value: " + record.value());
                logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
            }
        }
    }
}
