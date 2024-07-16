package com.kafka.learn.kafkastudy.repository;

import com.kafka.learn.kafkastudy.repository.dto.SuccessMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuccessMessageRepository extends ReactiveMongoRepository<SuccessMessage, String> {
}
