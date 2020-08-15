package pl.mstefanczuk.stockmarkettrading.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "\"user\"")
@Data
public class User {

    @Id
    private Long id;
    private String login;
}
