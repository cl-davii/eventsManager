package com.davi.eventsManager.models.repositories;

import com.davi.eventsManager.models.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    Event findByNickname(String nickname);
}
