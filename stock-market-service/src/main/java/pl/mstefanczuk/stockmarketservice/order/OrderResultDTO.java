package pl.mstefanczuk.stockmarketservice.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResultDTO {
    private Long instrumentId;
    private Long userId;
    private Integer typeId;
    private BigDecimal amount;
    private BigDecimal price;
    private LocalDateTime priceUpdateTime;
    private LocalDateTime dateTime;
}
