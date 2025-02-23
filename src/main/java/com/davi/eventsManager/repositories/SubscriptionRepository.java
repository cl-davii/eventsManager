package com.davi.eventsManager.repositories;

import com.davi.eventsManager.dto.SubscriptionRankingItem;
import com.davi.eventsManager.entities.Event;
import com.davi.eventsManager.entities.Subscription;
import com.davi.eventsManager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Subscription findByEventAndSubscriber(Event event, User subscriber);

    @Query(value = "SELECT COUNT(subscription_number), indication_user_id, name FROM subscriptions INNER JOIN users on indication_user_id = users.user_id WHERE indication_user_id IS NOT NULL AND subscriptions.event_id = :eventId GROUP BY subscriptions.subscription_number, indication_user_id, users.name ORDER BY subscription_number DESC", nativeQuery = true)
    List<SubscriptionRankingItem> generateRanking(@Param("eventId") Integer eventId);

}
