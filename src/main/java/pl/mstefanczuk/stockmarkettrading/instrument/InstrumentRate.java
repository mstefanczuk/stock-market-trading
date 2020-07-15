package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class InstrumentRate {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name="instrument_id", nullable=false)
    private Instrument instrument;
    private BigDecimal buyingRate;
    private BigDecimal sellingRate;
    private LocalDateTime lastUpdateTime;
}
