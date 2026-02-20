package sn.discover.discoversenegal.entities;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Informations générales ---
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EventStatus status = EventStatus.BROUILLON;

    // --- Dates et horaires ---
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    private boolean recurring;

    // ex: "Chaque vendredi", "Tous les week-ends"
    private String recurrencePattern;

    // --- Localisation ---
    @Column(nullable = false)
    private String location;

    private String address;

    private String city;

    private String region;

    private Double latitude;

    private Double longitude;

    // --- Capacité et billets ---
    private Integer maxParticipants;

    private Integer currentParticipants;

    private boolean freeEntry;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private String currency;

    private String ticketUrl;

    // --- Médias ---
    private String coverImageUrl;

    @ElementCollection
    @CollectionTable(name = "event_images", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    private String videoUrl;

    // --- Contacts et liens ---
    private String contactEmail;

    private String contactPhone;

    private String website;

    private String facebookUrl;

    private String instagramUrl;

    // --- Tags et langues ---
    @ElementCollection
    @CollectionTable(name = "event_tags", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "tag")
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "event_languages", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "language")
    @Builder.Default
    private List<String> languages = new ArrayList<>();

    // --- Organisateur ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    // --- Méta ---
    private boolean featured;

    private boolean verified;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.currentParticipants == null) this.currentParticipants = 0;
        if (this.currency == null) this.currency = "XOF";
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
