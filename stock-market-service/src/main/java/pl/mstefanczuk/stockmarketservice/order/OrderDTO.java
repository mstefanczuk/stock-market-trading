package pl.mstefanczuk.stockmarketservice.order;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class OrderDTO {
    Long instrumentId;
    Long userId;
    Integer typeId;
    BigDecimal amount;
}
