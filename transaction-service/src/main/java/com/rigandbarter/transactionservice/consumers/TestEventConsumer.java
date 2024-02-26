package com.rigandbarter.transactionservice.consumers;

import com.rigandbarter.eventservice.events.DemoEvent;
import com.rigandbarter.eventservice.events.TestEvent;
import com.rigandbarter.eventservice.model.RBEvent;
import com.rigandbarter.eventservice.components.RBEventConsumer;
import com.rigandbarter.eventservice.components.RBEventConsumerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestEventConsumer {

    @Autowired
    RBEventConsumerFactory rbEventConsumerFactory;

    private RBEventConsumer testEventConsumer;
    private RBEventConsumer demoEventConsumer;

    public TestEventConsumer(RBEventConsumerFactory rbEventConsumerFactory) {
        this.rbEventConsumerFactory = rbEventConsumerFactory;
        initConsumers();
    }

    public void initConsumers() {
        testEventConsumer = rbEventConsumerFactory.createConsumer(TestEvent.class, TestEventConsumer::handleTest);
        demoEventConsumer = rbEventConsumerFactory.createConsumer(DemoEvent.class, TestEventConsumer::handleDemo);

        testEventConsumer.start();
        demoEventConsumer.start();
    }

    private static Void handleTest(RBEvent evt) {
        if(!(evt instanceof TestEvent))
            return null;

        TestEvent testEvent = (TestEvent)evt;
        System.out.println("Id: " + testEvent.getId());
        System.out.println(testEvent.getAdditionalInfo());
        return null;
    }

    private static Void handleDemo(RBEvent evt) {
        if(!(evt instanceof DemoEvent))
            return null;

        DemoEvent demoEvent = (DemoEvent) evt;
        System.out.println("Id: " + demoEvent.getId());
        System.out.println(demoEvent.getAdditionalNumber());
        return null;
    }
}
