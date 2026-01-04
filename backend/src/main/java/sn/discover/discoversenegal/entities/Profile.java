package sn.discover.discoversenegal.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String displayName;
    private String bio;
    private String city;
    private String photoUrl;
    private boolean verified;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

