package com.davi.eventsManager.contollers;

import com.davi.eventsManager.dto.ErrorMessage;
import com.davi.eventsManager.dto.SubscriptionResponse;
import com.davi.eventsManager.entities.Subscription;
import com.davi.eventsManager.entities.User;
import com.davi.eventsManager.exceptions.EventNotFound;
import com.davi.eventsManager.exceptions.SubscriptionConflict;
import com.davi.eventsManager.exceptions.UserIndicatorNotFound;
import com.davi.eventsManager.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userId}"})
    public ResponseEntity<?> createSubscription(
            @PathVariable String prettyName,
            @RequestBody User subscriber,
            @PathVariable(required = false) Integer userId
    ) {
        try {
            SubscriptionResponse subscription = subscriptionService.createNewSubscription(prettyName, subscriber, userId);
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
}
