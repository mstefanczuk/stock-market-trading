package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Instrument {

    @Id
    private Long id;
    private String name;
}
