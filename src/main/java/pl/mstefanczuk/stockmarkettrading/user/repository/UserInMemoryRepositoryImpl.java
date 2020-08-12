package pl.mstefanczuk.stockmarkettrading.user.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserInMemoryRepositoryImpl implements UserInMemoryRepository {

    private final UserRepository userDbRepository;
    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(users.get(login));
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId((long) users.size() + 1);
        }
        users.put(user.getLogin(), user);
        userDbRepository.save(user);
        return user;
    }
}
