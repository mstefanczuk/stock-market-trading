package pl.mstefanczuk.stockmarkettrading.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    Long instrumentId;
    Long userId;
    Integer typeId;
    BigDecimal amount;
}
