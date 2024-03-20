package avlyakulov.timur.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString()
@NamedQueries({
        @NamedQuery(name = "HQL_DeleteSessionById",
                query = "delete from Session where id = :sessionId"),
        @NamedQuery(name = "HQL_GetUserByHisSession",
        query = "select user from Session where id = :sessionId"),
        @NamedQuery(name = "HQL_DeleteSessionByUserId",
        query = "delete from Session where user.id = :userId")
})
public class Session {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR) // hibernate 6 way
    private UUID id;

    private LocalDateTime expiresAt;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    public Session(UUID id, User user) {
        this.id = id;
        this.expiresAt = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);
        this.user = user;
    }
}