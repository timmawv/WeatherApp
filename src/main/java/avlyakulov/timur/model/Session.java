package avlyakulov.timur.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Session {

    @Id
    @Column(columnDefinition = "VARCHAR")
    private UUID id;

    private LocalDateTime expiresAt;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

}