package avlyakulov.timur.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@NamedQueries({
        @NamedQuery(name = "HQL_FindUserByUsername",
                query = "from User where login = :userLogin")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String login;

    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(Integer id) {
        this.id = id;
    }
}