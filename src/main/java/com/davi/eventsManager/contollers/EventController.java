package com.davi.eventsManager.contollers;

import com.davi.eventsManager.entities.Event;
import com.davi.eventsManager.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<Event> addNewEvent(@RequestBody Event event) {
        Event newEvent = eventService.addNewEvent(event);
        return ResponseEntity.ok().body(newEvent);
    }

    @GetMapping("/events")
    public List<Event> findAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/events/{prettyName}")
    public ResponseEntity<Event> findByPrettyName(@PathVariable String prettyName) {
        Event eventPrettyName = eventService.findByPrettyName(prettyName);
        if (eventPrettyName != null) {
            return ResponseEntity.ok().body(eventPrettyName);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/events/{prettyName}")
    public ResponseEntity<Event> deleteByPrettyName(@PathVariable String prettyName) {
        Event eventPrettyName = eventService.findByPrettyName(prettyName);
        if (eventPrettyName != null) {
            eventService.deleteByPrettyName(prettyName);
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
