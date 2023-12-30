package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {

    private static final Logger logger = LoggerFactory.getLogger(ProducerDemo.class);

    public static void main(String[] args) {

        logger.info("Hello World");

        final String topic = "first_topic";

        // create Producer Properties
        Properties properties = new Properties();

        // connect to Kafka Server
        properties.setProperty(KafkaKeys.BOOTSTRAP_SERVERS, "localhost:9092");

        // set producer properties
        properties.setProperty(KafkaKeys.KEY_SERIALIZER, StringSerializer.class.getName());
        properties.setProperty(KafkaKeys.VALUE_SERIALIZER, StringSerializer.class.getName());

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // create a Producer record
        ProducerRecord<String, String> record = new ProducerRecord<>(
                topic,
                "Hello, This is ProducerDemo"
        );

        // send data
        producer.send(record);

        logger.info("successfully sent record");

        // tell the producer to send all data and block until done -- synchronous
        producer.flush();

        // flush and close the program
        producer.close();

    }
}
