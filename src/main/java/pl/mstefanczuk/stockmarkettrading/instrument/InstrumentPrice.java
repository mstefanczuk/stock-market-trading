package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InstrumentPrice {

    @Id
    private Long id;
    private Instrument instrument;
    private BigDecimal price;
    private LocalDateTime stockServiceLastUpdateTime;
    private LocalDateTime lastUpdateTime;
}
