package com.analyst.util;

import com.analyst.db.entity.ClientTask;
import com.analyst.db.entity.SubscriptionExchangeEvent;
import com.analyst.db.entity.TopicSubscription;
import com.analyst.db.entity.TypeEvent;
import com.analyst.dto.MessageEventDto;
import com.analyst.dto.taskDto.TaskDto;

public class Adapter {
    public static MessageEventDto toMessageEvent(SubscriptionExchangeEvent subscriptionExchangeEvent, TypeEvent typeEvent) {
        return new MessageEventDto(
                subscriptionExchangeEvent.getExchange(),
                subscriptionExchangeEvent.getCurrencyPair(),
                typeEvent,
                subscriptionExchangeEvent.getTopicSubscription()
        );
    }

    public static ClientTask toGeneralSubscription(TaskDto taskDto, TopicSubscription topicSubscription) {
        return new ClientTask(
                taskDto.getTopicTask(),
                topicSubscription,
                taskDto.getExchanges(),
                taskDto.getCurrencyPairs()
        );
    }
}
