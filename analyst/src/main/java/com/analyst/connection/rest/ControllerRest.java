package com.analyst.connection.rest;

import com.analyst.dto.StatusTaskDto;
import com.analyst.dto.taskDto.VirtualTradeTaskDto;
import com.analyst.service.VirtualTradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag(name = "Public endpoints (not need authentication) MS Analyst.")
public class ControllerRest {
    final VirtualTradeService virtualTradeService;

    @Autowired
    public ControllerRest(@Qualifier("virtualTradeServiceImpl") VirtualTradeService virtualTradeService) {
        this.virtualTradeService = virtualTradeService;
    }

    @GetMapping("/virtualTrade/{clientID}")
    @Operation(description = "Getting tasks ids of all virtual trades.")
    public List<String> getVirtualTrades(@PathVariable("clientID") String clientID) {
        return virtualTradeService.getAllIdTasks(clientID);
    }

    //TODO This feature needs to be fully implemented.
    // At the moment, a task is being created that is saved in the database and sent to the backend of the
    // exchange connectors. And using RabbitMq we get the necessary data for the implementation of virtual trading.
    @PostMapping("/virtualTrade")
    @Operation(description = "Endpoint for creating task \"virtual trades\" (needs full implementation).")
    public StatusTaskDto createVirtualTrades(@RequestBody VirtualTradeTaskDto virtualTradeTaskDto) {
        return virtualTradeService.startTrades(virtualTradeTaskDto);
    }

    @DeleteMapping("/virtualTrade")
    @Operation(description = "Endpoint for deleting task \"virtual trades\".")
    public StatusTaskDto deleteTrades(@RequestParam String clientTaskID,
                                      @RequestParam String clientID) {
        return virtualTradeService.stopTrades(clientTaskID, clientID);
    }
}
