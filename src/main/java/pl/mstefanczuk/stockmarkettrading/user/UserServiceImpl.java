package pl.mstefanczuk.stockmarkettrading.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.stockmarkettrading.user.repository.UserRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> save(String login) {
        User user = new User();
        user.setLogin(login);
        return userRepository.save(user);
    }
}
