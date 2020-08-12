package pl.mstefanczuk.stockmarkettrading.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.stockmarkettrading.user.repository.UserInMemoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserInMemoryRepository userRepository;

    @Override
    public User save(String login) {
        User user;
        Optional<User> userOptional = userRepository.findByLogin(login);
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new User();
            user.setLogin(login);
            userRepository.save(user);
        }
        return user;
    }
}
