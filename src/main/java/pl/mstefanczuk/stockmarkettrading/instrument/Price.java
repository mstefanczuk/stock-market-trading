package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Price {
    private BigDecimal value;
    private LocalDateTime updateTime;
}
