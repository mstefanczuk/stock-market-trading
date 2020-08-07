package pl.mstefanczuk.stockmarketservice.price;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Price {
    private BigDecimal value;
    private long updateTime;
}
