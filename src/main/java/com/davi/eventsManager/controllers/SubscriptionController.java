package com.davi.eventsManager.controllers;

import com.davi.eventsManager.models.dtos.ErrorMessage;
import com.davi.eventsManager.models.dtos.SubscriptionResponse;
import com.davi.eventsManager.models.entities.Subscription;
import com.davi.eventsManager.models.entities.User;
import com.davi.eventsManager.models.exceptions.EventNotFound;
import com.davi.eventsManager.models.exceptions.SubscriptionConflict;
import com.davi.eventsManager.models.exceptions.UserIndicatorNotFound;
import com.davi.eventsManager.models.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping({"/subscription/{nickname}", "/subscription/{nickname}/{id}"})
    public ResponseEntity<?> createSubscription(@PathVariable String nickname, @RequestBody User subscriber, @PathVariable(required = false) Integer id) {
        try {
            SubscriptionResponse subscription = subscriptionService.createSubscription(nickname, subscriber, id);
            if (subscription != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EventNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(e.getMessage()));
        } catch (SubscriptionConflict e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(e.getMessage()));
        } catch (UserIndicatorNotFound e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping("/subscription/all")
    public List<Subscription> listAllSubscriptions() {
        return subscriptionService.listAllSubscriptions();
    }

    @GetMapping("/subscription/{nickname}/ranking")
    public ResponseEntity<?> getRankingByEvent(@PathVariable String nickname) {
        try {
            return ResponseEntity.ok(subscriptionService.getCompleteRanking(nickname).subList(0, 3));
        } catch (EventNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping("/subscription/{nickname}/ranking/{id}")
    public ResponseEntity<?> generateRankingByEventAndUser(@PathVariable String nickname, @PathVariable Integer id) {
        try {
            return ResponseEntity.ok(subscriptionService.getRankingByUser(nickname, id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/subscription/{nickname}/{id}")
    public ResponseEntity<?> deleteSubscriptionFromEvent(@PathVariable String nickname, @PathVariable Integer id) {
        subscriptionService.deleteUserFromEvent(nickname, id);
        return ResponseEntity.ok().body("User " + id + " successfully deleted");
    }
}
