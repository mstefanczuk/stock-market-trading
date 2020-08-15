package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.mstefanczuk.stockmarkettrading.order.OrderService;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InstrumentController {

    private final InstrumentService instrumentService;
    private final OrderService orderService;

    @PostMapping(path = "/instrument/save", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<UserInstrument>> save(@RequestBody List<UserInstrument> instruments) {
        List<UserInstrument> savedInstruments = instrumentService.saveAll(instruments);
        orderService.subscribe(savedInstruments.get(0).getUser());
        return Flux.just(instruments);
    }
}
