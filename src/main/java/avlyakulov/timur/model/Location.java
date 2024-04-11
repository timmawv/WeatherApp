package avlyakulov.timur.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "locations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "latitude", "longitude"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Location(String name, BigDecimal latitude, BigDecimal longitude, User user) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }
}