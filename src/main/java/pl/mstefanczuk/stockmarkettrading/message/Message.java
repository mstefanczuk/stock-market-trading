package pl.mstefanczuk.stockmarkettrading.message;

import lombok.Data;

@Data
public class Message {
    //prawdopodobnie do wyrzucenia, ale zostawiam zeby nie zapomniec jak dziala rozwiazanie z websocketami

    private String from;
    private String text;

}
