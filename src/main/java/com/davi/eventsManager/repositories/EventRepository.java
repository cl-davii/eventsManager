package com.davi.eventsManager.repositories;

import com.davi.eventsManager.entities.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Integer> {

    Event findByPrettyName(String prettyName);
}
