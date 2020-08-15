package pl.mstefanczuk.stockmarkettrading.order;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Table(value = "\"order\"")
@Data
public class Order {

    @Id
    private Long id;
    private Long userId;
    private Long instrumentId;
    private BigDecimal amount;
    private Integer typeId;
    private BigDecimal localPrice;
    private LocalDateTime localPriceUpdateTime;
    private LocalDateTime stockServicePriceUpdateTime;
    private BigDecimal realStockServicePrice;
    private LocalDateTime realStockServicePriceUpdateTime;
    private LocalDateTime requestDateTime;
    private LocalDateTime responseDateTime;

    public enum Type {
        BUY(1),
        SELL(2);

        public final Integer id;

        private Type(Integer id) {
            this.id = id;
        }
    }
}
