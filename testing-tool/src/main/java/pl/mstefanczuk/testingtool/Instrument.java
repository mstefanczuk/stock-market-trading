package pl.mstefanczuk.testingtool;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Instrument {
    private Long id;
    private String name;
}
