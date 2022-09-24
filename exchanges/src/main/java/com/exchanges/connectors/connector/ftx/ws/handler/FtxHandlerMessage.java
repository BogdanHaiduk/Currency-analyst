package com.exchanges.connectors.connector.ftx.ws.handler;

import com.exchanges.connectors.connector.ftx.FtxAdapter;
import com.exchanges.connectors.connector.ftx.ws.dto.FtxChannel;
import com.exchanges.connectors.connector.ftx.ws.dto.FtxWSTypeMessage;
import com.exchanges.connectors.connector.ftx.ws.dto.orderBook.FtxWsOrderBook;
import com.exchanges.connectors.core.ws.handler.HandlerMessage;
import com.exchanges.connectors.core.ws.handler.orderbook.HandlerOrderBook;
import com.exchanges.dto.OrderBook;
import com.exchanges.util.JsonUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxHandlerMessage extends HandlerMessage {
    static final String ORDER_BOOK_CHANNEL_NAME = "\"channel\":\"orderbook\"";

    @Autowired
    public FtxHandlerMessage(HandlerOrderBook handlerOrderBook) {
        super(handlerOrderBook);
    }

    @Override
    public String checkChannel(String jsonPayload) {
        if (jsonPayload != null) {
            if (jsonPayload.contains(ORDER_BOOK_CHANNEL_NAME)) {
                return FtxChannel.ORDER_BOOK.getName();
            }
        }

        return "";
    }

    @Override
    public String handleMessageByChannel(String channel, String jsonPayload) {
        if (jsonPayload.contains(FtxWSTypeMessage.PARTIAL.getNameType())
                || jsonPayload.contains(FtxWSTypeMessage.UPDATE.getNameType())) {

            if (channel.equals(FtxChannel.ORDER_BOOK.getName()))
                return handleMessageFtxOrderBook(jsonPayload);
        }

        return "";
    }

    private String handleMessageFtxOrderBook(String jsonPayload) {
        FtxWsOrderBook ftxWsOrderBook = JsonUtils.fromJson(jsonPayload, FtxWsOrderBook.class);
        OrderBook orderBook = FtxAdapter.toOrderBook(ftxWsOrderBook);

        if (ftxWsOrderBook.getType().equals(FtxWSTypeMessage.PARTIAL))
            orderBook = getHandlerOrderBook().setSnapshotOrderBook(orderBook);

        if (ftxWsOrderBook.getType().equals(FtxWSTypeMessage.UPDATE))
            orderBook = getHandlerOrderBook().update(orderBook);

        return (JsonUtils.toJson(orderBook));
    }
}
