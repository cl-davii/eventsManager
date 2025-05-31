package com.davi.eventsManager.models.exceptions;

public class SubscriptionConflict extends RuntimeException {

    public SubscriptionConflict(String message) {
        super(message);
    }
}
