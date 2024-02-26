package com.rigandbarter.eventservice.model;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaEventProducerImpl extends RBEventProducer {

    @Value("${rb.event.producer.kafka.url")
    private final String kafkaUrl = "localhost:9092";

    private final KafkaTemplate<String, RBEvent> kafkaTemplate;

    public KafkaEventProducerImpl(Class<? extends RBEvent> eventType) {
        super(eventType);

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:55899");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        ProducerFactory<String, RBEvent> factory = new DefaultKafkaProducerFactory<>(props);
        this.kafkaTemplate = new KafkaTemplate<>(factory);
    }

    @Override
    public void send(RBEvent event) {
        if(event.getClass() != this.eventType)
            return;

        String topic = event.getClass().getSimpleName();
        CompletableFuture<SendResult<String, RBEvent>> future = kafkaTemplate.send(topic, event);
        future.whenComplete((result, ex) -> {
            System.out.println("Result: " + result);
            System.out.println("Exception: " + ex);
//            if(ex != null)
//                log.error("Failed to send event: " + event.getId().toString() + "; " + ex.getMessage());
        });
    }
}
