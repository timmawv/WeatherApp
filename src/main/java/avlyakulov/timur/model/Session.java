package avlyakulov.timur.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions",
        indexes = {
                @Index(name = "idx_user_session", columnList = "expires_at")
        })
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Session {

    @Id
    private String id;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Transient
    private int minutesSessionExist = 30;

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