package com.rigandbarter.eventservice.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;

@Service
public class KakfaEventConsumerImpl extends RBEventConsumer {


    @Value("${rb.event.kafka.url")
    private final String kafkaUrl = "localhost:55899-var";

    private final String queueName;
    private final ObjectMapper objectMapper;

    private ConcurrentMessageListenerContainer listenerContainer;

    public KakfaEventConsumerImpl(String queueName, Function<RBEvent, Void> handlerFunction, Class<? extends RBEvent> type) {
        super(handlerFunction, type);

        this.queueName = queueName;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();

        Map<String, Object> consumerConfig = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, "http://localhost:55899",
                GROUP_ID_CONFIG, "TestEventId"
        );

        DefaultKafkaConsumerFactory<String, String> kafkaConsumerFactory =
                new DefaultKafkaConsumerFactory<>(
                        consumerConfig,
                        new StringDeserializer(),
                        new StringDeserializer());

        ContainerProperties containerProperties = new ContainerProperties(queueName);
        containerProperties.setMessageListener(
                (MessageListener<String, String>) record -> handleSerializedEvent(record.value()));

        listenerContainer = new ConcurrentMessageListenerContainer<>(
                        kafkaConsumerFactory,
                        containerProperties);
    }


//    @KafkaListener(topics = "#queueName")
//    @KafkaListener(topics = "TestEvent")
    public void handleSerializedEvent(String event) {
        try {
            var evt = objectMapper.readValue(event, this.eventType);
            handleEvent(evt);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvent(RBEvent event) {
        this.handlerFunction.apply(event);
    }

    @Override
    public void start() {
        listenerContainer.start();
    }

    @Override
    public void stop() {
        listenerContainer.stop();
    }
}
