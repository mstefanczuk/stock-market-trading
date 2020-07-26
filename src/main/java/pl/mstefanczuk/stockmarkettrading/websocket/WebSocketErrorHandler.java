package pl.mstefanczuk.stockmarkettrading.websocket;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class WebSocketErrorHandler {

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public Message handleException(Throwable exception) {
        return Message.builder()
                .body(exception.getMessage())
                .time(LocalDateTime.now().toString())
                .build();
    }
}
