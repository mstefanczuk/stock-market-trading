package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServicePriceDTO {
    private Double value;
    private long updateTime;
}
