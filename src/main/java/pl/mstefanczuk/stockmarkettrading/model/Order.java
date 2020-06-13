package pl.mstefanczuk.stockmarkettrading.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @ManyToOne
    @JoinColumn(name="instrument_id", nullable=false)
    private Instrument instrument;
    private BigDecimal amount;
    private Type type;
    private BigDecimal buyingRate;
    private BigDecimal sellingRate;
    private LocalDateTime dateTime;

    public enum Type {
        BUY, SELL
    }
}
