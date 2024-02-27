package com.rigandbarter.eventlibrary.components.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.eventlibrary.components.RBEventConsumer;
import com.rigandbarter.eventlibrary.config.RBEventProperties;
import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.eventlibrary.model.RBEventResult;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;

@Component
public class KakfaEventConsumerImpl extends RBEventConsumer {
    private final String queueName;
    private final ObjectMapper objectMapper;
    private final ConcurrentMessageListenerContainer listenerContainer;

    public KakfaEventConsumerImpl(String queueName,
                                  Function<RBEvent, RBEventResult> handlerFunction, Class<? extends RBEvent> type,
                                  Environment environment,
                                  ObjectMapper objectMapper) {

        super(handlerFunction, type);
        this.queueName = queueName;
        this.objectMapper = objectMapper;

        String kafkaUrl = environment.getProperty(RBEventProperties.RB_EVENT_BROKER_URL);
        if(kafkaUrl == null)
            throw new RuntimeException("RB Event Broker URL Not Set");

        Map<String, Object> consumerConfig = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, kafkaUrl,
                GROUP_ID_CONFIG, "RigAndBarterEventId"
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

    private void handleSerializedEvent(String event) {
        try {
            var evt = objectMapper.readValue(event, this.eventType);
            handleEvent(evt);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private String getQueueName() {
        return this.queueName;
    }
}
