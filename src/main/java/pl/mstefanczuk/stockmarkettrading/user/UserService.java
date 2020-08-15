package pl.mstefanczuk.stockmarkettrading.user;

import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> save(String login);
}
