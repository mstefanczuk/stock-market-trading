package pl.mstefanczuk.stockmarkettrading.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentService;
import pl.mstefanczuk.stockmarkettrading.instrument.Price;
import pl.mstefanczuk.stockmarkettrading.instrument.ServicePriceDTO;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
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
        return LongServicePriceMap.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        LongServicePriceMap response = (LongServicePriceMap) payload;
        Map<Long, Price> convertedResponse = response.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> fromDto(e.getValue())));
        instrumentService.setCurrentPrices(convertedResponse);
    }

    private Price fromDto(ServicePriceDTO dto) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getUpdateTime()), ZoneId.systemDefault());
        return new Price(BigDecimal.valueOf(dto.getValue()), dateTime);
    }

    private static class LongServicePriceMap extends HashMap<Long, ServicePriceDTO> {
    }
}
