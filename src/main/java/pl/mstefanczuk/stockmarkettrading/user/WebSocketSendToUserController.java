package pl.mstefanczuk.stockmarkettrading.user;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.mstefanczuk.stockmarkettrading.message.Message;

import java.time.LocalDateTime;

@Controller
public class WebSocketSendToUserController {

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public Message handleException(Throwable exception) {
        return Message.builder()
                .body(exception.getMessage())
                .time(LocalDateTime.now().toString())
                .build();
    }
}
