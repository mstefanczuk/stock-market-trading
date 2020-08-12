package pl.mstefanczuk.stockmarkettrading.order;

import lombok.Data;
import pl.mstefanczuk.stockmarkettrading.instrument.Instrument;
import pl.mstefanczuk.stockmarkettrading.user.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name="order_generator", sequenceName = "order_seq", allocationSize = 1)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @ManyToOne
    @JoinColumn(name="instrument_id", nullable=false)
    private Instrument instrument;
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name="type_id", nullable=false)
    private OrderType type;
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
