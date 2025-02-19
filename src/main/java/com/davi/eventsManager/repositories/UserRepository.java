package com.davi.eventsManager.repositories;

import com.davi.eventsManager.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);

}
