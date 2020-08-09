package pl.mstefanczuk.testingtool;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class StockMarketTradingWebsocketClient {

    @PostConstruct
    public void connect() {
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new StockMarketTradingStompSessionHandler();
        stompClient.connect("ws://192.168.99.100:8080/greeting", sessionHandler);
    }
}
