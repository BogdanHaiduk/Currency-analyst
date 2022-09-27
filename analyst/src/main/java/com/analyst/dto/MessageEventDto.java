package com.analyst.dto;

import com.analyst.db.entity.TopicSubscription;
import com.analyst.db.entity.TypeEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageEventDto {
    String exchange;
    String currencyPair;
    TypeEvent typeEvent;
    TopicSubscription topic;
}
