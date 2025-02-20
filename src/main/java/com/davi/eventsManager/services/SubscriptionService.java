package com.davi.eventsManager.services;

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
        return new SubscriptionResponse(saveSubscription.getSubscriptionNumber(), "http://www.codecraft.com/subscription/" + saveSubscription.getEvent().getPrettyName() + "/" + saveSubscription.getSubscriber().getUserId());}
}
