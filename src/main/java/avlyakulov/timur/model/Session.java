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
@EqualsAndHashCode
public class Session {

    @Transient
    private int minutesSessionExist = 30;

    @Id
    //@JdbcTypeCode(SqlTypes.VARCHAR) // hibernate 6 way
    private String id;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    private void setExpiresAt() {
        this.expiresAt = LocalDateTime.now().plusMinutes(minutesSessionExist);
    }

    public Session(String id, User user) {
        this.id = id;
        this.user = user;
    }

    public Session(String id, LocalDateTime expiresAt, User user) {
        this.id = id;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}