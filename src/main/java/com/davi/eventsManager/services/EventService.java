package com.davi.eventsManager.services;

import com.davi.eventsManager.entities.Event;
import com.davi.eventsManager.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event addNewEvent(Event event) {
        event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return (List<Event>) eventRepository.findAll();
    }

    public Event findByPrettyName(String prettyName) {
        return eventRepository.findByPrettyName(prettyName);
    }

    public void deleteByPrettyName(String prettyName) {
        eventRepository.delete(findByPrettyName(prettyName));
    }

}
