package com.davi.eventsManager.controllers;

import com.davi.eventsManager.models.entities.Event;
import com.davi.eventsManager.models.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event newEvent = eventService.newEvent(event);
        return ResponseEntity.ok().body(newEvent);
    }

    @GetMapping("/events")
    public List<Event> listAllEvents() {
        return eventService.listAllEvents();
    }

    @GetMapping("/events/{nickname}")
    public ResponseEntity<Event> findByNickname(@PathVariable String nickname) {
        Event nicknameEvent = eventService.findByNickname(nickname);
        if (nicknameEvent != null) {
            return ResponseEntity.ok().body(nicknameEvent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/events/{nickname}")
    public ResponseEntity<Event> deleteByNickname(@PathVariable String nickname) {
        Event nicknameEvent = eventService.findByNickname(nickname);
        if (nicknameEvent != null) {
            eventService.deleteByNickname(nickname);
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
