package com.analyst.service.impl;

import com.analyst.db.entity.*;
import com.analyst.db.repository.ClientRecordRepository;
import com.analyst.db.repository.SubscriptionExchangeEventRepository;
import com.analyst.dto.StatusTaskDto;
import com.analyst.exception.TaskClientException;
import com.analyst.service.TaskSubscribeService;
import com.analyst.util.Adapter;
import com.analyst.util.JsonUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskSubscribeServiceImpl implements TaskSubscribeService {
    final RabbitTemplate rabbitTemplate;
    final SubscriptionExchangeEventRepository subscriptionExchangeEventRepository;
    final ClientRecordRepository clientRecordRepository;
    Set<ClientRecord> allClientsTasks;
    Set<SubscriptionExchangeEvent> subscriptionsExchangeEvents;

    @Value("${rabbit.queue.analyst.request.to.exchanges}")
    String analystRequestToExchanges;

    @Autowired
    public TaskSubscribeServiceImpl(RabbitTemplate rabbitTemplate, SubscriptionExchangeEventRepository subscriptionExchangeEventRepository, ClientRecordRepository clientRecordRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.subscriptionExchangeEventRepository = subscriptionExchangeEventRepository;
        this.clientRecordRepository = clientRecordRepository;
    }

    @PostConstruct
    public void init() {
        this.subscriptionsExchangeEvents = Collections.synchronizedSet(new HashSet<>(subscriptionExchangeEventRepository.findAll()));
        this.allClientsTasks = Collections.synchronizedSet(new HashSet<>(clientRecordRepository.findAll()));
    }

    @Override
    public StatusTaskDto createClientTask(ClientTask newClientTask, String clientID) {
        Set<SubscriptionExchangeEvent> newSubscriptionExchangeEvents = createSubscriptionExchangeEvent(newClientTask);

        ClientRecord clientRecordByClientID = allClientsTasks.stream()
                .filter(clientRecord -> clientRecord.getClientID().equals(clientID))
                .findFirst()
                .orElse(new ClientRecord(clientID, new HashSet<>(), new HashSet<>()));

        ClientRecord updateClientRecord = addClientTaskToClientRecord(clientRecordByClientID, newClientTask);

        newSubscriptionExchangeEvents.forEach(newSubscriptionExchangeEvent ->
                updateClientRecord.getSubscribeToMSExchange()
                        .add(preparingAndSendingSubscription(newSubscriptionExchangeEvent, newClientTask.getTaskID()))
        );

        clientRecordRepository.save(updateClientRecord);

        return new StatusTaskDto(newClientTask.getTaskID(), newClientTask.getTopicTask(), StatusTask.CREATED);
    }

    @Override
    public StatusTaskDto deleteClientTasks(String taskId, String clientID) {
        if (taskId != null && clientID != null) {
            ClientRecord clientRecord = searchClientRecordByClientIdTaskId(taskId, clientID);

            if (clientRecord != null) {
                subscriptionsExchangeEvents.stream()
                        .filter(subscriptionExchangeEvent -> subscriptionExchangeEvent.getSetTasksID().contains(taskId))
                        .forEach(subscriptionExchangeEvent -> {
                            subscriptionExchangeEvent.getSetTasksID().remove(taskId);
                            clientRecord.getSubscribeToMSExchange().remove(subscriptionExchangeEvent.getId());
                            subscriptionExchangeEventRepository.save(subscriptionExchangeEvent);
                        });
                ClientTask clientTaskByTaskId = clientRecord.getClientTasks()
                        .stream()
                        .filter(clientTask -> clientTask.getTaskID().equals(taskId))
                        .findFirst()
                        .orElse(null);

                clientRecord.getClientTasks().remove(clientTaskByTaskId);
                clientRecordRepository.save(clientRecord);
                checkSubscribesExchangeEvents();

                return new StatusTaskDto(clientID, clientTaskByTaskId.getTopicTask(), StatusTask.DELETED);
            }

            throw new TaskClientException("Client ID or task ID not correct.");
        }

        throw new TaskClientException("Task ID and Client ID must not be null.");
    }

    private ClientRecord searchClientRecordByClientIdTaskId(String taskId, String clientID) {
        return allClientsTasks.stream()
                .filter(clientRecord -> clientRecord.getClientID().equals(clientID))
                .filter(clientRecord -> clientRecord.getClientTasks().stream()
                        .anyMatch(clientRecord1 -> clientRecord1.getTaskID().equals(taskId)))
                .findFirst()
                .orElse(null);
    }

    private ClientRecord addClientTaskToClientRecord(ClientRecord clientRecord, ClientTask newClientTask) {
        if (clientRecord.getClientTasks() == null)
            clientRecord.setClientTasks(new HashSet<>());

        if (clientRecord.getSubscribeToMSExchange() == null)
            clientRecord.setSubscribeToMSExchange(new HashSet<>());

        clientRecord.getClientTasks()
                .forEach(clientTask -> {
                    if (clientTask.equals(newClientTask))
                        throw new TaskClientException("Such a task has already been created");
                });

        clientRecord.getClientTasks().add(newClientTask);
        allClientsTasks.add(clientRecord);

        return clientRecord;
    }

    private Set<SubscriptionExchangeEvent> createSubscriptionExchangeEvent(ClientTask clientTask) {
        Set<SubscriptionExchangeEvent> newSubscriptionExchangeEvents = new HashSet<>();

        clientTask.getExchanges().forEach(exchange ->
                clientTask.getCurrencyPairs().forEach(currencyPair -> {
                    SubscriptionExchangeEvent newSubscriptionExchangeEvent = new SubscriptionExchangeEvent(
                            exchange,
                            currencyPair,
                            clientTask.getTopicSubscription()
                    );

                    newSubscriptionExchangeEvents.add(newSubscriptionExchangeEvent);
                }));

        return newSubscriptionExchangeEvents;
    }

    private void checkSubscribesExchangeEvents() {
        subscriptionsExchangeEvents.stream()
                .filter(subscriptionExchangeEvent -> subscriptionExchangeEvent.getSetTasksID().isEmpty())
                .collect(Collectors.toList())
                .forEach(deleteSubscriptionExchangeEvent -> {
                    subscriptionsExchangeEvents.remove(deleteSubscriptionExchangeEvent);
                    subscriptionExchangeEventRepository.delete(deleteSubscriptionExchangeEvent);
                    sentUnsubscribe(deleteSubscriptionExchangeEvent);
                });
    }

    private String preparingAndSendingSubscription(SubscriptionExchangeEvent newSubscriptionExchangeEvent, String taskID) {
        SubscriptionExchangeEvent subscriptionExchangeEventByFilter = subscriptionsExchangeEvents.stream()
                .filter(subscriptionExchangeEvent -> subscriptionExchangeEvent.equals(newSubscriptionExchangeEvent))
                .findFirst()
                .orElse(null);

        if (subscriptionExchangeEventByFilter != null) {
            subscriptionExchangeEventByFilter.getSetTasksID().add(taskID);
            subscriptionExchangeEventRepository.save(subscriptionExchangeEventByFilter);

            return subscriptionExchangeEventByFilter.getId();
        }

        return sentSubscribe(newSubscriptionExchangeEvent, taskID);
    }

    private String sentSubscribe(SubscriptionExchangeEvent newSubscriptionExchangeEvent, String taskID) {
        newSubscriptionExchangeEvent.getSetTasksID().add(taskID);
        subscriptionsExchangeEvents.add(newSubscriptionExchangeEvent);
        subscriptionExchangeEventRepository.save(newSubscriptionExchangeEvent);
        sentMessageEvent(newSubscriptionExchangeEvent, TypeEvent.SUBSCRIBE);

        return newSubscriptionExchangeEvent.getId();
    }

    private void sentUnsubscribe(SubscriptionExchangeEvent unSubscriptionExchangeEvent) {
        if (!subscriptionsExchangeEvents.contains(unSubscriptionExchangeEvent)) {
            sentMessageEvent(unSubscriptionExchangeEvent, TypeEvent.UNSUBSCRIBE);
        }
    }

    private void sentMessageEvent(SubscriptionExchangeEvent newSubscriptionExchangeEvent, TypeEvent typeEvent) {
        String jsonMessageEvent = JsonUtils.toJson(Adapter.toMessageEvent(newSubscriptionExchangeEvent, typeEvent));
        rabbitTemplate.convertAndSend(analystRequestToExchanges, jsonMessageEvent);
    }
}
