package pl.mstefanczuk.stockmarkettrading.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutputMessage {
    //prawdopodobnie do wyrzucenia, ale zostawiam zeby nie zapomniec jak dziala rozwiazanie z websocketami

    private String from;
    private String text;
    private String time;

}
