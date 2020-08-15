package pl.mstefanczuk.stockmarketservice.price;

import reactor.core.publisher.Flux;

import java.util.Map;

public interface InstrumentPriceService {

    Price getCurrent(Long id);

    Flux<Map<Long, Price>> getCurrentPrices();
}
