package pl.mstefanczuk.stockmarkettrading.user;

import lombok.Data;
import pl.mstefanczuk.stockmarkettrading.instrument.Instrument;
import pl.mstefanczuk.stockmarkettrading.user.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class UserInstrument {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @ManyToOne
    @JoinColumn(name="instrument_id", nullable=false)
    private Instrument instrument;
    private BigDecimal limit;
    private BigDecimal amount;
}
