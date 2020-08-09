package pl.mstefanczuk.testingtool;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserInstrument {

    private Long id;
    private User user;
    private Instrument instrument;
    private BigDecimal tradingAmount;
    private BigDecimal amount;
    private BigDecimal balance;
}
