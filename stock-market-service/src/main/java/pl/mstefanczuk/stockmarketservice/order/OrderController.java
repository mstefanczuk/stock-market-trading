package pl.mstefanczuk.stockmarketservice.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.mstefanczuk.stockmarketservice.price.InstrumentPriceService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final InstrumentPriceService instrumentPriceService;

    @PostMapping("purchase")
    public ResponseEntity<OrderResultDTO> purchase(@RequestBody OrderDTO order) {
        return ResponseEntity.ok(getOrderResultDTO(order));
    }

    @PostMapping("sell")
    public ResponseEntity<OrderResultDTO> sell(@RequestBody OrderDTO order) {
        return ResponseEntity.ok(getOrderResultDTO(order));
    }

    private OrderResultDTO getOrderResultDTO(OrderDTO order) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant
                .ofEpochMilli(instrumentPriceService.getCurrent(order.getInstrumentId()).getUpdateTime()),
                ZoneId.systemDefault());
        return new OrderResultDTO(order.getInstrumentId(),
                order.getUserId(), order.getTypeId(), order.getAmount(),
                instrumentPriceService.getCurrent(order.getInstrumentId()).getValue(), dateTime, LocalDateTime.now());
    }
}
