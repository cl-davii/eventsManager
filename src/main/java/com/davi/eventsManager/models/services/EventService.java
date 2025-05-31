package com.davi.eventsManager.models.services;

import com.davi.eventsManager.models.entities.Event;
import com.davi.eventsManager.models.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event newEvent(Event event) {
        event.setNickname(event.getTitle().toLowerCase().replaceAll(" ", "-"));
        return eventRepository.save(event);
    }

    public List<Event> listAllEvents() {
        return eventRepository.findAll();
    }

    public Event findByNickname(String nickname) {
        return eventRepository.findByNickname(nickname);
    }

    public void deleteByNickname(String nickname) {
        eventRepository.delete(findByNickname(nickname));
    }
}
