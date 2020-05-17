package pl.mstefanczuk.stockmarkettrading.controller;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.mstefanczuk.stockmarkettrading.model.Message;
import pl.mstefanczuk.stockmarkettrading.model.OutputMessage;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketSendToUserController {

    @MessageMapping("/message")
    @SendToUser("/queue/reply")
    public OutputMessage processMessageFromClient(final Message message, Principal principal) throws Exception {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public OutputMessage handleException(Throwable exception) {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage("", exception.getMessage(), time);
    }
}
