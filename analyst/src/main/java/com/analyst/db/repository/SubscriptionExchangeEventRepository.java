package com.analyst.db.repository;

import com.analyst.db.entity.SubscriptionExchangeEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionExchangeEventRepository extends MongoRepository<SubscriptionExchangeEvent, String> {

}
