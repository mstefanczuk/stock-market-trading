package pl.mstefanczuk.stockmarkettrading.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentService;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class StockMarketServiceStompSessionHandler extends StompSessionHandlerAdapter {

    private final InstrumentService instrumentService;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/current-prices", this);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Map.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Map<String, Double> response = (Map<String, Double>) payload;
        Map<Long, BigDecimal> convertedResponse = response.entrySet().stream()
                .collect(Collectors.toMap(k -> Long.valueOf(k.getKey()), e -> BigDecimal.valueOf(e.getValue())));
        instrumentService.setCurrentPrices(convertedResponse);
    }
}
