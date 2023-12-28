package org.example;

public interface KafkaKeys {

    String BOOTSTRAP_SERVERS = "bootstrap.servers";

    // Producer keys
    String KEY_SERIALIZER = "key.serializer";
    String VALUE_SERIALIZER = "value.serializer";
    String BATCH_SIZE = "batch.size";
    String PARTITIONER_CLASS = "partitioner.class";

    // Consumer keys
    String KEY_DESERIALIZER = "key.deserializer";
    String VALUE_DESERIALIZER = "value.deserializer";
    String GROUP_ID = "group.id";
    String AUTO_OFFSET_RESET = "auto.offset.reset";
}
