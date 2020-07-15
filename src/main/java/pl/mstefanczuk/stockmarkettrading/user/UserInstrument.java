package pl.mstefanczuk.stockmarkettrading.user;

import lombok.Data;
import pl.mstefanczuk.stockmarkettrading.instrument.Instrument;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class UserInstrument {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @ManyToOne
    @JoinColumn(name="instrument_id", nullable=false)
    private Instrument instrument;
    private BigDecimal limit;
    private BigDecimal amount;
}
