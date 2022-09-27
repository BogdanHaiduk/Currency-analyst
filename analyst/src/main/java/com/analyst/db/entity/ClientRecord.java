package com.analyst.db.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientRecord {
    @Id
    String clientID;
    Set<ClientTask> clientTasks;
    Set<String> subscribeToMSExchange;
}
