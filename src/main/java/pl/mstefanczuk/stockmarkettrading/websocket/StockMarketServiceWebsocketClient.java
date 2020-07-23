package pl.mstefanczuk.stockmarkettrading.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentService;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class StockMarketServiceWebsocketClient {

    private final InstrumentService instrumentService;

    @PostConstruct
    public void connect() {
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new StockMarketServiceStompSessionHandler(instrumentService);
        stompClient.connect("ws://stock-market-service:8080/greeting", sessionHandler);
    }
}
