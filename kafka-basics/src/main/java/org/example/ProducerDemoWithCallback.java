package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;

public class ProducerDemoWithCallback {

    private static final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);

    public static void main(String[] args) {

        logger.info("I am a Kafka Producer");

        final String topic = "second_topic";

        // create Producer Properties
        Properties properties = new Properties();

        // connect to Kafka Server
        properties.setProperty(KafkaKeys.BOOTSTRAP_SERVERS, "localhost:9092");

        // set producer properties
        properties.setProperty(KafkaKeys.KEY_SERIALIZER, StringSerializer.class.getName());
        properties.setProperty(KafkaKeys.VALUE_SERIALIZER, StringSerializer.class.getName());

        properties.setProperty(KafkaKeys.BATCH_SIZE, "400");
        properties.setProperty(KafkaKeys.PARTITIONER_CLASS, RoundRobinPartitioner.class.getName());
        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // send data
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 30; i++) {

                // create a Producer record
                ProducerRecord<String, String> record = new ProducerRecord<>(
                        topic,
                        "Hello, This is ProducerDemo" + i
                );

                producer.send(record, (recordMetadata, e) -> {
                    // executes every time a record successfully sent or an exception is thrown
                    if (Objects.isNull(e)) {

                        logger.info("Received new meta data \n" +
                                "Topic: " + recordMetadata.topic() + "\n" +
                                "Partition: " + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "Timestamp: " + recordMetadata.timestamp() + "\n"
                        );
                    } else {
                        logger.error("Error while producing ", e);
                    }
                });
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // tell the producer to send all data and block until done -- synchronous
        producer.flush();

        // flush and close the program
        producer.close();

    }
}
