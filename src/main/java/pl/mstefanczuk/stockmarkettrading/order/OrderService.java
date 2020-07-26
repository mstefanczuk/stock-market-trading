package pl.mstefanczuk.stockmarkettrading.order;

import pl.mstefanczuk.stockmarkettrading.user.User;

import java.security.Principal;

public interface OrderService {

    void startListening(User user, Principal principal);
}
