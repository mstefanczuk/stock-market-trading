package pl.mstefanczuk.stockmarkettrading.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResultDTO {
    Long instrumentId;
    Long userId;
    Integer typeId;
    BigDecimal amount;
    BigDecimal price;
    LocalDateTime dateTime;
}
