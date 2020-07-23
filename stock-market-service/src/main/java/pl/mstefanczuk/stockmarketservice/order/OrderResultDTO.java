package pl.mstefanczuk.stockmarketservice.order;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class OrderResultDTO {
    Long instrumentId;
    Long userId;
    Integer typeId;
    BigDecimal amount;
    BigDecimal price;
    LocalDateTime dateTime;
}
