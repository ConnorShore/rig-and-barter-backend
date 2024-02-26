package com.rigandbarter.transactionservice.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.eventservice.model.RBEvent;
import com.rigandbarter.eventservice.model.RBEventConsumer;
import com.rigandbarter.eventservice.model.RBEventConsumerFactory;
import com.rigandbarter.transactionservice.model.TestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestEventConsumer {

//    private final RBEventConsumer testEventConsumer;
    private final ObjectMapper objectMapper;

//    public TestEventConsumer(ObjectMapper objectMapper) {
//        testEventConsumer = RBEventConsumerFactory.createConsumer(TestEvent.class, TestEventConsumer::handle);
//    }

    private static Void handle(RBEvent evt) {
        if(!(evt instanceof TestEvent testEvent)) {
            System.out.println("INCORRECT EVENT TYPE: " + evt.getClass().getSimpleName());
            return null;
        }

        System.out.println("Recieved event: " + testEvent.getId());
        System.out.println("Event info: " + testEvent.getAdditionalInfo());
        return null;
    }

    @KafkaListener(topics="TestEvent")
    public void handleEvent(String serializedEvent) {
        try {
            TestEvent testEvent = objectMapper.readValue(serializedEvent, TestEvent.class);
            System.out.println("Here");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
