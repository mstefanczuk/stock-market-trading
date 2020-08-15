package pl.mstefanczuk.stockmarkettrading.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentService;
import pl.mstefanczuk.stockmarkettrading.instrument.Price;
import pl.mstefanczuk.stockmarkettrading.instrument.ServicePriceDTO;
import pl.mstefanczuk.stockmarkettrading.order.OrderDTO;
import pl.mstefanczuk.stockmarkettrading.order.OrderResultDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StockMarketServiceWebClient {

    private final InstrumentService instrumentService;

    private final WebClient client;

    public StockMarketServiceWebClient(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
        client = WebClient.create("http://stock-market-service:8080");
    }

    @PostConstruct
    public void subscribe() {
        Flux<LongServicePriceMap> eventStream = client.get()
                .uri("/current-prices")
                .retrieve()
                .bodyToFlux(LongServicePriceMap.class);

        eventStream.subscribe(this::setCurrentPrices);
    }

    public Mono<OrderResultDTO> order(OrderDTO dto) {
        return client.post()
                .uri("/purchase")
                .body(Mono.just(dto), OrderDTO.class)
                .retrieve()
                .bodyToMono(OrderResultDTO.class);
    }

    private void setCurrentPrices(LongServicePriceMap map) {
        Map<Long, Price> convertedResponse = map.entrySet().stream()
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
