package com.analyst.service;

import com.analyst.db.entity.ClientTask;
import com.analyst.dto.StatusTaskDto;

public interface TaskSubscribeService {
    StatusTaskDto createClientTask(ClientTask newClientTask, String clientID);

    StatusTaskDto deleteClientTasks(String clientTasksID, String clientID);
}
