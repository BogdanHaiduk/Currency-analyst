package com.analyst.db.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientTask {
    String taskID;
    TopicTask topicTask;
    TopicSubscription topicSubscription;
    List<String> exchanges;
    List<String> currencyPairs;

    public ClientTask(TopicTask topicTask, TopicSubscription topicSubscription, List<String> exchanges, List<String> currencyPairs) {
        this.taskID = UUID.randomUUID().toString();
        this.topicTask = topicTask;
        this.topicSubscription = topicSubscription;
        this.exchanges = exchanges;
        this.currencyPairs = currencyPairs;
    }

    //Equals and hashCode work without taskID comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientTask that = (ClientTask) o;
        return topicTask == that.topicTask && topicSubscription == that.topicSubscription && Objects.equals(exchanges, that.exchanges) && Objects.equals(currencyPairs, that.currencyPairs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicTask, topicSubscription, exchanges, currencyPairs);
    }
}
