package com.analyst.service.impl;

import com.analyst.db.entity.ClientRecord;
import com.analyst.db.entity.ClientTask;
import com.analyst.db.entity.TopicSubscription;
import com.analyst.db.entity.TopicTask;
import com.analyst.db.repository.ClientRecordRepository;
import com.analyst.dto.StatusTaskDto;
import com.analyst.dto.taskDto.VirtualTradeTaskDto;
import com.analyst.exception.TaskClientException;
import com.analyst.service.TaskSubscribeService;
import com.analyst.service.VirtualTradeService;
import com.analyst.util.Adapter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VirtualTradeServiceImpl implements VirtualTradeService {
    final TaskSubscribeService taskSubscribeServiceImpl;
    final ClientRecordRepository clientRecordRepository;

    public VirtualTradeServiceImpl(TaskSubscribeServiceImpl taskSubscribeServiceImpl,
                                   ClientRecordRepository clientRecordRepository) {
        this.taskSubscribeServiceImpl = taskSubscribeServiceImpl;
        this.clientRecordRepository = clientRecordRepository;
    }

    @Override
    public StatusTaskDto startTrades(VirtualTradeTaskDto virtualTradeTaskDto) {
        if (virtualTradeTaskDto != null) {
            if (virtualTradeTaskDto.getTopicTask().equals(TopicTask.VIRTUAL_TRADE)) {
                ClientTask clientTask = Adapter.toGeneralSubscription(virtualTradeTaskDto, TopicSubscription.ORDER_BOOK);

                return taskSubscribeServiceImpl.createClientTask(clientTask, virtualTradeTaskDto.getClientID());
            }

            throw new TaskClientException("Task topic is not correct");
        }

        throw new TaskClientException("Request body not be empty.");
    }

    @Override
    public StatusTaskDto stopTrades(String clientTasksID, String clientID) {
        return taskSubscribeServiceImpl.deleteClientTasks(clientTasksID, clientID);
    }

    @Override
    public List<String> getAllIdTasks(String clientID) {
        ClientRecord clientRecord = clientRecordRepository.findAllByClientID(clientID);

        if (clientRecord != null)
            return clientRecordRepository.findAllByClientID(clientID).getClientTasks()
                    .stream()
                    .filter(clientTask -> clientTask.getTopicTask().equals(TopicTask.VIRTUAL_TRADE))
                    .map(ClientTask::getTaskID)
                    .collect(Collectors.toList());

        throw new TaskClientException("There is no client with this id.");
    }
}
