package com.training.notificationsvc.repository;

import com.training.notificationsvc.domain.Recipient;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecipientRepository extends MongoRepository<Recipient,String> {
}
