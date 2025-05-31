package com.davi.eventsManager.models.repositories;

import com.davi.eventsManager.models.dtos.SubscriptionRankingItem;
import com.davi.eventsManager.models.entities.Event;
import com.davi.eventsManager.models.entities.Subscription;
import com.davi.eventsManager.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Subscription findByEventAndSubscriber(Event event, User user);

    @Query(value = "SELECT COUNT(subscription_number), indication_user_id, name FROM subscriptions " +
            "INNER JOIN users on indication_user_id=users.user_id " +
            "WHERE indication_user_id IS NOT NULL AND subscriptions.event_id=id " +
            "GROUP BY subscriptions.subscription_number, indication_user_id, users.name " +
            "ORDER BY subscription_number DESC",
            nativeQuery = true)
    List<SubscriptionRankingItem> createRanking(@Param("id") Integer id);
}
