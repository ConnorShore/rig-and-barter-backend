package com.rigandbarter.eventservice.model;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class KakfaEventConsumerImpl extends RBEventConsumer {

    private final String queueName;

    public KakfaEventConsumerImpl(String queueName, Function<RBEvent, Void> handlerFunction, Class<? extends RBEvent> type) {
        super(handlerFunction, type);
        this.queueName = queueName;
    }
    @Override
    @KafkaListener(topics = "#queueName")
    public void handleEvent(RBEvent event) {
        if(event.getClass() != eventType)
            return;

        //do stuff needed for kafka

        this.handlerFunction.apply(event);
    }
}
