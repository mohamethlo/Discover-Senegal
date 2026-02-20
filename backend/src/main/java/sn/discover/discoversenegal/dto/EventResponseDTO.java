package sn.discover.discoversenegal.dto;


import lombok.*;
import sn.discover.discoversenegal.entities.EventCategory;
import sn.discover.discoversenegal.entities.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String shortDescription;
    private EventCategory category;
    private EventStatus status;

    // Dates
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean recurring;
    private String recurrencePattern;

    // Localisation
    private String location;
    private String address;
    private String city;
    private String region;
    private Double latitude;
    private Double longitude;

    // Capacité
    private Integer maxParticipants;
    private Integer currentParticipants;
    private Integer availableSpots;
    private boolean freeEntry;
    private BigDecimal price;
    private String currency;
    private String ticketUrl;

    // Médias
    private String coverImageUrl;
    private List<String> imageUrls;
    private String videoUrl;

    // Contacts
    private String contactEmail;
    private String contactPhone;
    private String website;
    private String facebookUrl;
    private String instagramUrl;

    // Tags / Langues
    private List<String> tags;
    private List<String> languages;

    // Organisateur
    private OrganizerInfo organizer;

    // Méta
    private boolean featured;
    private boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrganizerInfo {
        private Long id;
        private String displayName;
        private String photoUrl;
        private boolean verified;
    }
}