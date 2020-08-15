package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.Data;
import org.springframework.data.annotation.Id;
import pl.mstefanczuk.stockmarkettrading.user.User;

import java.math.BigDecimal;

@Data
public class UserInstrument {

    @Id
    private Long id;
    private User user;
    private Instrument instrument;
    private BigDecimal tradingAmount;
    private BigDecimal amount;
    private BigDecimal balance;
}
