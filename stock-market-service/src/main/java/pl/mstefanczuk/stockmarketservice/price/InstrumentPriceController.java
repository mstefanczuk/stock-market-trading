package pl.mstefanczuk.stockmarketservice.price;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
public class InstrumentPriceController {

    private final InstrumentPriceService instrumentPriceService;

    public InstrumentPriceController(InstrumentPriceService instrumentPriceService) {
        this.instrumentPriceService = instrumentPriceService;
    }

    @GetMapping(path = "/current-prices", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<Long, Price>> getCurrentPrices() {
        return instrumentPriceService.getCurrentPrices();
    }
}
