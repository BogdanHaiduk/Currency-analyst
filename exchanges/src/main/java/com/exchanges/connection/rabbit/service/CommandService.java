package com.exchanges.connection.rabbit.service;

import com.exchanges.dto.MessageEvent;

public interface CommandService {
    void handleMessage(MessageEvent messageEvent);
}
