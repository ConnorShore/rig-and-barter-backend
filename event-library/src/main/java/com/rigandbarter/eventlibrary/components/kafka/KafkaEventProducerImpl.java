package com.rigandbarter.eventlibrary.components.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.eventlibrary.components.RBEventProducer;
import com.rigandbarter.eventlibrary.config.RBEventProperties;
import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.eventlibrary.model.RBEventResult;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Component
public class KafkaEventProducerImpl extends RBEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaEventProducerImpl(Class<? extends RBEvent> eventType,
                                  Environment environment,
                                  ObjectMapper objectMapper) {
        super(eventType);
        this.objectMapper = objectMapper;

        //TODO: Separate out config to method/class
        String kafkaUrl = environment.getProperty(RBEventProperties.RB_EVENT_BROKER_URL);
        if(kafkaUrl == null)
            throw new RuntimeException("RB Event Broker URL Not Set");

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        ProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(props);
        this.kafkaTemplate = new KafkaTemplate<>(factory);
    }

    @Override
    public void send(RBEvent event) {
        send(event, null);
    }

    @Override
    public void send(RBEvent event, Function<String, Void> onErrorFunc) {
        if(event.getClass() != this.eventType) {
            String msg = String.format(
                    "Incompatible event types: [%s] != [%s]",
                    event.getClass().getSimpleName(),
                    this.eventType.getSimpleName()
            );

            if(onErrorFunc != null)
                onErrorFunc.apply(msg);
            return;
        }

        String topic = event.getClass().getSimpleName();
        String value;
        try {
            value = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            if(onErrorFunc != null)
                onErrorFunc.apply(e.getMessage());
            return;
        }

        if(onErrorFunc == null) {
            kafkaTemplate.send(topic, value);
            return;
        }

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, value);
        future.whenComplete((result, ex) -> {
            if(ex != null)
                onErrorFunc.apply(ex.getMessage());
        });
    }
}
