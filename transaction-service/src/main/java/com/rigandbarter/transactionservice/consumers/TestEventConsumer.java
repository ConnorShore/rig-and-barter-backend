package com.rigandbarter.transactionservice.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.eventservice.model.RBEvent;
import com.rigandbarter.eventservice.model.RBEventConsumer;
import com.rigandbarter.eventservice.model.RBEventConsumerFactory;
import com.rigandbarter.transactionservice.model.TestEvent;
import org.springframework.stereotype.Component;

@Component
public class TestEventConsumer {

    private final RBEventConsumer testEventConsumer;
//    private ObjectMapper objectMapper;

    public TestEventConsumer() {
        testEventConsumer = RBEventConsumerFactory.createConsumer(TestEvent.class, TestEventConsumer::handle);
        testEventConsumer.start();
        System.out.println();
//        objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules();
    }

    private static Void handle(RBEvent evt) {
        if(!(evt instanceof TestEvent))
            return null;

        TestEvent testEvent = (TestEvent)evt;
        System.out.println("Id: " + testEvent.getId());
        System.out.println(testEvent.getAdditionalInfo());
        return null;
    }

//    @KafkaListener(topics="TestEvent")
//    public void handleEvent(String serializedEvent) {
//        try {
//            TestEvent testEvent = objectMapper.readValue(serializedEvent, TestEvent.class);
//            System.out.println("Here");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
