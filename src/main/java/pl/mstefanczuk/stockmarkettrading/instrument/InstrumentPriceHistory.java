package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class InstrumentPriceHistory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="instrument_id", nullable=false)
    private Instrument instrument;
    private BigDecimal price;
    private LocalDateTime dateTime;
}
