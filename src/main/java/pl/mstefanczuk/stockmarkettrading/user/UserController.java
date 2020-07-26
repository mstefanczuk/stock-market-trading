package pl.mstefanczuk.stockmarkettrading.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @MessageMapping("/users/save")
    @SendToUser("/queue/login")
    public User login(final String login) {
        return userService.save(login);
    }
}
