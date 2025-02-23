package com.davi.eventsManager.services;

import com.davi.eventsManager.dto.SubscriptionRankingByUser;
import com.davi.eventsManager.dto.SubscriptionRankingItem;
import com.davi.eventsManager.dto.SubscriptionResponse;
import com.davi.eventsManager.entities.Event;
import com.davi.eventsManager.entities.Subscription;
import com.davi.eventsManager.entities.User;
import com.davi.eventsManager.exceptions.EventNotFound;
import com.davi.eventsManager.exceptions.SubscriptionConflict;
import com.davi.eventsManager.exceptions.UserIndicatorNotFound;
import com.davi.eventsManager.repositories.EventRepository;
import com.davi.eventsManager.repositories.SubscriptionRepository;
import com.davi.eventsManager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionResponse createNewSubscription(String eventName, User user, Integer userId) {
        // Recuperando o evento pelo nome
        Event event = eventRepository.findByPrettyName(eventName);
        if (event == null) {
            throw new EventNotFound("The event " + eventName + " does not exist.");
        }
        // Obtendo o email do usuário
        User userData = userRepository.findByEmail(user.getEmail());
        //Verificando se o email já está cadastrado no evento
        if (userData == null) {
            user = userRepository.save(user);
        }
        User indicator = null;
        if (userId != null) {
            indicator = userRepository.findById(userId).orElse(null);
            if (indicator == null) {
                throw new UserIndicatorNotFound("The indicator user " + userId + " does not exist.");
            }
        }
        Subscription subscription = new Subscription();
        subscription.setEvent(event);
        subscription.setSubscriber(user);
        subscription.setIndication(indicator);

        Subscription eventSubscriber = subscriptionRepository.findByEventAndSubscriber(event, userData);
        if (eventSubscriber != null) {
            assert userData != null;
            throw new SubscriptionConflict("The user " + userData.getName() + " already is registered in event " + event.getTitle() + ".");
        }
        Subscription saveSubscription = subscriptionRepository.save(subscription);
        return new SubscriptionResponse(saveSubscription.getSubscriptionNumber(), "http://www.codecraft.com/subscription/" + saveSubscription.getEvent().getPrettyName() + "/" + saveSubscription.getSubscriber().getUserId());
    }

    public List<SubscriptionRankingItem> getCompleteRanking(String prettyName) {
        Event event = eventRepository.findByPrettyName(prettyName);
        if (event == null) {
            throw new EventNotFound("The ranking of " + prettyName + " event does not exist.");
        }
        return subscriptionRepository.generateRanking(event.getEventId());
    }

    public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId) {
        List<SubscriptionRankingItem> ranking = getCompleteRanking(prettyName);
        SubscriptionRankingItem item = ranking.stream().filter(x -> x.userId().equals(userId)).findFirst().orElse(null);
        if (item == null) {
            throw new UserIndicatorNotFound("There are no subscriptions indicated by user " + userId + ".");
        }
        int position = IntStream.range(0, ranking.size()).filter(index -> ranking.get(index).userId().equals(userId)).findFirst().getAsInt();
        return new SubscriptionRankingByUser(item, position + 1);
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public void deleteUserFromEvent(@PathVariable String prettyName, @PathVariable Integer userId) {
        var item = subscriptionRepository.findAll().stream().filter(x -> x.getEvent().getPrettyName().equals(prettyName) && x.getSubscriber().getUserId().equals(userId)).toList();
        subscriptionRepository.deleteAll(item);
        }
}
