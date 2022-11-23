package microservices.book.multiplication.user;

import lombok.*;

import javax.persistence.*;

/**
 * Stores information to identify the user.
 */
@Entity(name = "game_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String alias;

    public User(final String userAlias) {
        this(null, userAlias);
    }
}
