package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ConsumerDemoWithShutdown {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerDemoWithShutdown.class);

    public static void main(String[] args) {

        logger.info("This is ConsumerDemo");

        final String topic = "first_topic";
        final String groupId = "kafka_programming_group";

        // create consumer properties
        Properties properties = new Properties();

        // connect to Kafka Server
        properties.setProperty(KafkaKeys.BOOTSTRAP_SERVERS, "localhost:9092");

        // set producer properties
        properties.setProperty(KafkaKeys.KEY_DESERIALIZER, StringDeserializer.class.getName());
        properties.setProperty(KafkaKeys.VALUE_DESERIALIZER, StringDeserializer.class.getName());

        properties.setProperty(KafkaKeys.GROUP_ID, groupId);
        properties.setProperty(KafkaKeys.AUTO_OFFSET_RESET, "earliest");

        // create the consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // get reference to main thread
        final Thread mainThread = Thread.currentThread();

        // adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            logger.info("Detected a shutdown, let's exit by calling consumer.wake()... ");

            consumer.wakeup();

            // join the main thread to allow the execution of the code in the main thread
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        try {
            // subscribe to a topic
            consumer.subscribe(List.of(topic));

            // poll for data
            while (true) {

                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord record : records) {

                    logger.info("Key: " + record.key() + ", Value: " + record.value());
                    logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
                }
            }
        } catch (WakeupException e) {
            logger.info("Consumer is starting to shutdown");
        } catch (Exception e) {
            logger.error("Excepted exception in the consumer ", e);
        } finally {
            consumer.close();
            logger.info("The consumer is now graceful shutdown");
        }
    }
}
