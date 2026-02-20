package sn.discover.discoversenegal.dto;


import jakarta.validation.constraints.*;
import lombok.*;
import sn.discover.discoversenegal.entities.EventCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 255, message = "Le titre doit contenir entre 3 et 255 caractères")
    private String title;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @Size(max = 500, message = "La description courte ne doit pas dépasser 500 caractères")
    private String shortDescription;

    @NotNull(message = "La catégorie est obligatoire")
    private EventCategory category;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime startDateTime;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime endDateTime;

    private boolean recurring;
    private String recurrencePattern;

    @NotBlank(message = "Le lieu est obligatoire")
    private String location;

    private String address;

    @NotBlank(message = "La ville est obligatoire")
    private String city;

    private String region;

    @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
    private Double latitude;

    @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
    private Double longitude;

    @Min(value = 1, message = "La capacité maximale doit être au moins 1")
    private Integer maxParticipants;

    private boolean freeEntry;

    @DecimalMin(value = "0.0", message = "Le prix ne peut pas être négatif")
    private BigDecimal price;

    private String currency;

    private String ticketUrl;

    private String coverImageUrl;

    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    private String videoUrl;

    @Email(message = "L'email de contact n'est pas valide")
    private String contactEmail;

    private String contactPhone;

    private String website;

    private String facebookUrl;

    private String instagramUrl;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private List<String> languages = new ArrayList<>();
}
