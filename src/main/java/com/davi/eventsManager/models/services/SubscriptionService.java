package com.davi.eventsManager.models.services;

import com.davi.eventsManager.models.dtos.SubscriptionRankingByUser;
import com.davi.eventsManager.models.dtos.SubscriptionRankingItem;
import com.davi.eventsManager.models.dtos.SubscriptionResponse;
import com.davi.eventsManager.models.entities.Event;
import com.davi.eventsManager.models.entities.Subscription;
import com.davi.eventsManager.models.entities.User;
import com.davi.eventsManager.models.exceptions.EventNotFound;
import com.davi.eventsManager.models.exceptions.SubscriptionConflict;
import com.davi.eventsManager.models.exceptions.UserIndicatorNotFound;
import com.davi.eventsManager.models.repositories.EventRepository;
import com.davi.eventsManager.models.repositories.SubscriptionRepository;
import com.davi.eventsManager.models.repositories.UserRepository;
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

    public SubscriptionResponse createSubscription(String eventNickname, User user, Integer id) {
        // Recuperando o evento pelo nome
        Event event = eventRepository.findByNickname(eventNickname);
        if (event == null) {
            throw new EventNotFound("The event " + eventNickname + " does not exist.");
        }
        // Obtendo o email do usuário
        User userData = userRepository.findByEmail(user.getEmail());
        //Verificando se o email já está cadastrado no evento
        if (userData == null) {
            user = userRepository.save(user);
        }
        User indicator = null;
        if (id != null) {
            indicator = userRepository.findById(id).orElse(null);
            if (indicator == null) {
                throw new UserIndicatorNotFound("The indicator user " + id + " does not exist.");
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
        return new SubscriptionResponse(saveSubscription.getSubscriptionNumber(), "http://www.codecraft.com/subscription/" + saveSubscription.getEvent().getNickname() + "/" + saveSubscription.getSubscriber().getId());
    }

    public List<SubscriptionRankingItem> getCompleteRanking(String nickname) {
        Event event = eventRepository.findByNickname(nickname);
        if (event == null) {
            throw new EventNotFound("The ranking of " + nickname + " event does not exist.");
        }
        return subscriptionRepository.createRanking(event.getId());
    }

    public SubscriptionRankingByUser getRankingByUser(String nickname, Integer id) {
        List<SubscriptionRankingItem> ranking = getCompleteRanking(nickname);
        SubscriptionRankingItem item = ranking.stream().filter(x -> x.id().equals(id)).findFirst().orElse(null);
        if (item == null) {
            throw new UserIndicatorNotFound("There are no subscriptions indicated by user " + id + ".");
        }
        int position = IntStream.range(0, ranking.size()).filter(index -> ranking.get(index).id().equals(id)).findFirst().getAsInt();
        return new SubscriptionRankingByUser(item, position + 1);
    }

    public List<Subscription> listAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public void deleteUserFromEvent(@PathVariable String nickname, @PathVariable Integer id) {
        var item = subscriptionRepository.findAll().stream().filter(x -> x.getEvent().getNickname().equals(nickname) && x.getSubscriber().getId().equals(id)).toList();
        subscriptionRepository.deleteAll(item);
    }
}
