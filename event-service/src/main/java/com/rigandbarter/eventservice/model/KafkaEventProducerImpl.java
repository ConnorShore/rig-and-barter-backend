package com.rigandbarter.eventservice.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaEventProducerImpl extends RBEventProducer {

    @Value("${rb.event.kafka.url")
    private final String kafkaUrl = "localhost:55899";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaEventProducerImpl(Class<? extends RBEvent> eventType, ObjectMapper objectMapper) {
        super(eventType);
        this.objectMapper = objectMapper;

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        ProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(props);
        this.kafkaTemplate = new KafkaTemplate<>(factory);
    }

    @Override
    public void send(RBEvent event) {
        if(event.getClass() != this.eventType)
            return;

        String topic = event.getClass().getSimpleName();
        String value;
        try {
            value = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            System.out.println("Error serializing object: " + event.getId());
            return;
        }

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, value);
        future.whenComplete((result, ex) -> {
            System.out.println("Result: " + result);
            System.out.println("Exception: " + ex);
//            if(ex != null)
//                log.error("Failed to send event: " + event.getId().toString() + "; " + ex.getMessage());
        });
    }
}
