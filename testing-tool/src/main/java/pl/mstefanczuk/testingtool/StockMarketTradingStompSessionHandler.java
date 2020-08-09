package pl.mstefanczuk.testingtool;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class StockMarketTradingStompSessionHandler extends StompSessionHandlerAdapter {

    @SneakyThrows
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        Instrument cdpInstrument = new Instrument(1L, "CDPROJEKT");
        Instrument teslaInstrument = new Instrument(2L, "TESLA");
        Instrument pgeInstrument = new Instrument(3L, "PGE");

        List<UserInstrument> userInstruments;
        User user = new User();

        for(int i = 1; i <= 100; i++) {

            user.setId((long) i);
            user.setLogin("user" + i);
            session.send("/users/save", "user" + i);
            Thread.sleep(1000);

            userInstruments = new ArrayList<>();
            userInstruments.add(createUserInstrument(user, cdpInstrument, BigDecimal.valueOf(100)));
            userInstruments.add(createUserInstrument(user, teslaInstrument, BigDecimal.valueOf(200)));
            userInstruments.add(createUserInstrument(user, pgeInstrument, BigDecimal.valueOf(300)));

            session.send("/instrument/save", userInstruments);
            if (i % 10 == 0) {
                log.info("Saved users: " + i);
            }

        }
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Object.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("received: " + payload.toString());
    }

    private UserInstrument createUserInstrument(User user, Instrument instrument, BigDecimal tradingAmount) {
        UserInstrument userInstrument = new UserInstrument();
        userInstrument.setAmount(BigDecimal.valueOf(1000));
        userInstrument.setTradingAmount(tradingAmount);
        userInstrument.setBalance(BigDecimal.valueOf(0));
        userInstrument.setInstrument(instrument);
        userInstrument.setUser(user);
        return userInstrument;
    }
}
