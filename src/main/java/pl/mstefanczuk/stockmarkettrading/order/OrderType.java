package pl.mstefanczuk.stockmarkettrading.order;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class OrderType {

    @Id
    private Integer id;
    private String name;
}
