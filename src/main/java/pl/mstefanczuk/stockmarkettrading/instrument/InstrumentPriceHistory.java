package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InstrumentPriceHistory {
    @Id
    private Long id;
    private Instrument instrument;
    private BigDecimal price;
    private LocalDateTime stockServiceUpdateTime;
    private LocalDateTime updateTime;
}
