package pl.mstefanczuk.testingtool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestingToolApplication {

    public static void main(String[] args) throws InterruptedException {

        WebClient client = WebClient.create("http://192.168.99.100:8080");

        Instrument cdpInstrument = new Instrument(1L, "CDPROJEKT");
        Instrument teslaInstrument = new Instrument(2L, "TESLA");
        Instrument pgeInstrument = new Instrument(3L, "PGE");

        List<UserInstrument> userInstruments;
        User user;

        log.info("Saved users: " + 0);

        for(int i = 1; i <= 100; i++) {

            user = client.post()
                    .uri("/users/save")
                    .body(Mono.just("user" + i), String.class)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            Thread.sleep(500);

            userInstruments = new ArrayList<>();
            userInstruments.add(createUserInstrument(user, cdpInstrument, BigDecimal.valueOf(100)));
            userInstruments.add(createUserInstrument(user, teslaInstrument, BigDecimal.valueOf(200)));
            userInstruments.add(createUserInstrument(user, pgeInstrument, BigDecimal.valueOf(300)));

            Flux<UserInstrumentList> fluxList = client.post()
                    .uri("/instrument/save")
                    .body(Mono.just(userInstruments), ArrayList.class)
                    .retrieve()
                    .bodyToFlux(UserInstrumentList.class);

            fluxList.subscribe(System.out::println);

            Thread.sleep(500);
            if (i % 10 == 0) {
                log.info("Saved users: " + i);
            }

        }
        Thread.sleep(300000);
    }


    private static UserInstrument createUserInstrument(User user, Instrument instrument, BigDecimal tradingAmount) {
        UserInstrument userInstrument = new UserInstrument();
        userInstrument.setAmount(BigDecimal.valueOf(1000));
        userInstrument.setTradingAmount(tradingAmount);
        userInstrument.setBalance(BigDecimal.valueOf(0));
        userInstrument.setInstrument(instrument);
        userInstrument.setUser(user);
        return userInstrument;
    }

    private static class UserInstrumentList extends ArrayList<UserInstrument> {}
}
