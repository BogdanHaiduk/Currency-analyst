package com.analyst.db.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionExchangeEvent {
    @Id
    String id;
    String exchange;
    String currencyPair;
    TopicSubscription topicSubscription;
    Set<String> setTasksID = new HashSet<>();

    public SubscriptionExchangeEvent() {
        this.id = UUID.randomUUID().toString();
    }

    public SubscriptionExchangeEvent(String exchange, String currencyPair, TopicSubscription topicSubscription) {
        this.id = UUID.randomUUID().toString();
        this.exchange = exchange;
        this.currencyPair = currencyPair;
        this.topicSubscription = topicSubscription;
    }

    //Equals and hashCode work without id comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionExchangeEvent subscriptionExchangeEvent = (SubscriptionExchangeEvent) o;
        return Objects.equals(exchange, subscriptionExchangeEvent.exchange) && Objects.equals(currencyPair, subscriptionExchangeEvent.currencyPair) && topicSubscription == subscriptionExchangeEvent.topicSubscription;
    }

    @Override
    public int hashCode() {
        return Objects.hash(exchange, currencyPair, topicSubscription);
    }
}