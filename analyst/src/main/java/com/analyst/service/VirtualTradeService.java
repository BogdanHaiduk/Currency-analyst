package com.analyst.service;

import com.analyst.dto.StatusTaskDto;
import com.analyst.dto.taskDto.VirtualTradeTaskDto;

import java.util.List;

public interface VirtualTradeService {
    StatusTaskDto startTrades(VirtualTradeTaskDto virtualTradeTaskDto);

    StatusTaskDto stopTrades(String clientTasksID, String clientID);

    List<String> getAllIdTasks(String clientID);
}
