package pl.mstefanczuk.stockmarkettrading.user;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.mstefanczuk.stockmarkettrading.message.OutputMessage;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @MessageMapping("/login")
    @SendToUser("/queue/reply")
    //ten OutputMessage do wywalenia/przerobienia
    public OutputMessage processMessageFromClient(final String login, Principal principal) {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        User user = new User();
        user.setLogin(login);
        userService.save(user);
        return new OutputMessage(login, "Zalogowano", time);
    }
}
