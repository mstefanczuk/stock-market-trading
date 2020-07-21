package pl.mstefanczuk.stockmarkettrading.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private String body;
    private String time;
}
