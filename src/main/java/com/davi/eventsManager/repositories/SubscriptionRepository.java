package com.davi.eventsManager.repositories;

import com.davi.eventsManager.entities.Event;
import com.davi.eventsManager.entities.Subscription;
import com.davi.eventsManager.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {

    Subscription findByEventAndSubscriber(Event event, User subscriber);
}
