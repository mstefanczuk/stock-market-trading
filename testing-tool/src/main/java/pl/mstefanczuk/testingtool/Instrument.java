package pl.mstefanczuk.testingtool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instrument {
    private Long id;
    private String name;
}
