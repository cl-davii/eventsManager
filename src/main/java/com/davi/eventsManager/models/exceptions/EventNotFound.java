package com.davi.eventsManager.models.exceptions;

public class EventNotFound extends RuntimeException {

    public EventNotFound(String message) {
        super(message);
    }
}
