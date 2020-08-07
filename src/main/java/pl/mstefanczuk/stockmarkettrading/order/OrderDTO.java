package pl.mstefanczuk.stockmarkettrading.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long instrumentId;
    private Long userId;
    private Integer typeId;
    private BigDecimal amount;
}
