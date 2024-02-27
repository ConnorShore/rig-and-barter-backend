package com.rigandbarter.eventlibrary.model;

import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class RBEventHandler {

    @Autowired
    protected RBEventConsumerFactory rbEventConsumerFactory;

    public RBEventHandler(RBEventConsumerFactory rbEventConsumerFactory) {
        _RBEventHandler(rbEventConsumerFactory, false);
    }

    public RBEventHandler(RBEventConsumerFactory rbEventConsumerFactory, boolean delayStart) {
        _RBEventHandler(rbEventConsumerFactory, delayStart);
    }

    private void _RBEventHandler(RBEventConsumerFactory rbEventConsumerFactory, boolean delayStart) {
        this.rbEventConsumerFactory = rbEventConsumerFactory;
        initializeConsumers();

        if(!delayStart)
            startConsumers();
    }

    @PreDestroy
    public void onDestroy() {
        stopConsumers();
    }

    public abstract void initializeConsumers();
    public abstract void startConsumers();
    public abstract void stopConsumers();
}
