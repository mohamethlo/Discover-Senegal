package sn.discover.discoversenegal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.discover.discoversenegal.dto.*;
import sn.discover.discoversenegal.entities.*;
import sn.discover.discoversenegal.repositories.UserRepository;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelService {
    
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public HotelResponseDTO createHotel(HotelCreateDTO dto) {
        log.info("Creating new hotel: {}", dto.getName());
        
        // Vérifier si l'email existe déjà
        if (dto.getEmail() != null && hotelRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Un hôtel avec cet email existe déjà");
        }
        
        // Récupérer le propriétaire si spécifié
        User owner = null;
        if (dto.getOwnerId() != null) {
            owner = userRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Propriétaire non trouvé"));
        }
        
        Hotel hotel = Hotel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .city(dto.getCity())
                .region(dto.getRegion())
                .postalCode(dto.getPostalCode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .website(dto.getWebsite())
                .category(dto.getCategory())
                .starRating(dto.getStarRating())
                .totalRooms(dto.getTotalRooms())
                .priceRangeMin(dto.getPriceRangeMin())
                .priceRangeMax(dto.getPriceRangeMax())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "XOF")
                .amenities(dto.getAmenities())
                .spokenLanguages(dto.getSpokenLanguages())
                .photoUrls(dto.getPhotoUrls())
                .culturalHighlights(dto.getCulturalHighlights())
                .sustainabilityPractices(dto.getSustainabilityPractices())
                .checkInTime(dto.getCheckInTime())
                .checkOutTime(dto.getCheckOutTime())
                .owner(owner)
                .verificationStatus(VerificationStatus.PENDING)
                .partnershipStatus(PartnershipStatus.PROSPECT)
                .active(true)
                .viewCount(0)
                .bookingCount(0)
                .build();
        
        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Hotel created successfully with ID: {}", savedHotel.getId());
        
        return mapToResponseDTO(savedHotel);
    }
    
    @Transactional
    public HotelResponseDTO updateHotel(Long id, HotelUpdateDTO dto) {
        log.info("Updating hotel with ID: {}", id);
        
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hôtel non trouvé"));
        
        // Mise à jour des champs
        if (dto.getName() != null) hotel.setName(dto.getName());
        if (dto.getDescription() != null) hotel.setDescription(dto.getDescription());
        if (dto.getAddress() != null) hotel.setAddress(dto.getAddress());
        if (dto.getCity() != null) hotel.setCity(dto.getCity());
        if (dto.getRegion() != null) hotel.setRegion(dto.getRegion());
        if (dto.getPostalCode() != null) hotel.setPostalCode(dto.getPostalCode());
        if (dto.getLatitude() != null) hotel.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) hotel.setLongitude(dto.getLongitude());
        if (dto.getEmail() != null) hotel.setEmail(dto.getEmail());
        if (dto.getPhone() != null) hotel.setPhone(dto.getPhone());
        if (dto.getWebsite() != null) hotel.setWebsite(dto.getWebsite());
        if (dto.getCategory() != null) hotel.setCategory(dto.getCategory());
        if (dto.getStarRating() != null) hotel.setStarRating(dto.getStarRating());
        if (dto.getTotalRooms() != null) hotel.setTotalRooms(dto.getTotalRooms());
        if (dto.getPriceRangeMin() != null) hotel.setPriceRangeMin(dto.getPriceRangeMin());
        if (dto.getPriceRangeMax() != null) hotel.setPriceRangeMax(dto.getPriceRangeMax());
        if (dto.getAmenities() != null) hotel.setAmenities(dto.getAmenities());
        if (dto.getSpokenLanguages() != null) hotel.setSpokenLanguages(dto.getSpokenLanguages());
        if (dto.getPhotoUrls() != null) hotel.setPhotoUrls(dto.getPhotoUrls());
        if (dto.getCulturalHighlights() != null) hotel.setCulturalHighlights(dto.getCulturalHighlights());
        if (dto.getSustainabilityPractices() != null) hotel.setSustainabilityPractices(dto.getSustainabilityPractices());
        if (dto.getCheckInTime() != null) hotel.setCheckInTime(dto.getCheckInTime());
        if (dto.getCheckOutTime() != null) hotel.setCheckOutTime(dto.getCheckOutTime());
        if (dto.getFeatured() != null) hotel.setFeatured(dto.getFeatured());
        if (dto.getActive() != null) hotel.setActive(dto.getActive());
        
        Hotel updatedHotel = hotelRepository.save(hotel);
        log.info("Hotel updated successfully");
        
        return mapToResponseDTO(updatedHotel);
    }
    
    @Transactional
    public HotelResponseDTO updatePartnership(Long id, PartnershipUpdateDTO dto) {
        log.info("Updating partnership for hotel ID: {}", id);
        
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hôtel non trouvé"));
        
        if (dto.getPartnershipStatus() != null) {
            hotel.setPartnershipStatus(dto.getPartnershipStatus());
        }
        if (dto.getPartnershipStartDate() != null) {
            hotel.setPartnershipStartDate(dto.getPartnershipStartDate());
        }
        if (dto.getPartnershipEndDate() != null) {
            hotel.setPartnershipEndDate(dto.getPartnershipEndDate());
        }
        if (dto.getCommissionRate() != null) {
            hotel.setCommissionRate(dto.getCommissionRate());
        }
        if (dto.getSpecialOffers() != null) {
            hotel.setSpecialOffers(dto.getSpecialOffers());
        }
        
        Hotel updatedHotel = hotelRepository.save(hotel);
        log.info("Partnership updated successfully");
        
        return mapToResponseDTO(updatedHotel);
    }
    
    @Transactional
    public HotelResponseDTO updateVerification(Long id, VerificationUpdateDTO dto) {
        log.info("Updating verification status for hotel ID: {}", id);
        
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hôtel non trouvé"));
        
        hotel.setVerificationStatus(dto.getVerificationStatus());
        hotel.setLastVerifiedAt(LocalDateTime.now());
        
        Hotel updatedHotel = hotelRepository.save(hotel);
        log.info("Verification status updated to: {}", dto.getVerificationStatus());
        
        return mapToResponseDTO(updatedHotel);
    }
    
    @Transactional(readOnly = true)
    public HotelResponseDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hôtel non trouvé"));
        return mapToResponseDTO(hotel);
    }
    
    @Transactional
    public HotelResponseDTO incrementViewCount(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hôtel non trouvé"));
        hotel.setViewCount(hotel.getViewCount() + 1);
        return mapToResponseDTO(hotelRepository.save(hotel));
    }
    
    @Transactional
    public HotelResponseDTO incrementBookingCount(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hôtel non trouvé"));
        hotel.setBookingCount(hotel.getBookingCount() + 1);
        return mapToResponseDTO(hotelRepository.save(hotel));
    }
    
    @Transactional(readOnly = true)
    public List<HotelResponseDTO> getAllHotels() {
        return hotelRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HotelResponseDTO> getHotelsByCity(String city) {
        return hotelRepository.findByCity(city).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HotelResponseDTO> getHotelsByRegion(String region) {
        return hotelRepository.findByRegion(region).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HotelResponseDTO> getFeaturedHotels() {
        return hotelRepository.findByFeaturedTrueAndActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HotelResponseDTO> getTopRatedHotels() {
        return hotelRepository.findTopRatedHotels().stream()
                .limit(10)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HotelResponseDTO> getMostPopularHotels() {
        return hotelRepository.findMostPopularHotels().stream()
                .limit(10)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HotelResponseDTO> getActivePartners() {
        return hotelRepository.findActivePartners().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<HotelResponseDTO> searchHotels(HotelSearchDTO searchDTO) {
        return hotelRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (searchDTO.getCity() != null) {
                predicates.add(criteriaBuilder.equal(root.get("city"), searchDTO.getCity()));
            }
            if (searchDTO.getRegion() != null) {
                predicates.add(criteriaBuilder.equal(root.get("region"), searchDTO.getRegion()));
            }
            if (searchDTO.getCategory() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), searchDTO.getCategory()));
            }
            if (searchDTO.getMinStarRating() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("starRating"), searchDTO.getMinStarRating()));
            }
            if (searchDTO.getMaxStarRating() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("starRating"), searchDTO.getMaxStarRating()));
            }
            if (searchDTO.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("priceRangeMin"), searchDTO.getMinPrice()));
            }
            if (searchDTO.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("priceRangeMax"), searchDTO.getMaxPrice()));
            }
            if (searchDTO.getPartnershipStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("partnershipStatus"), searchDTO.getPartnershipStatus()));
            }
            if (searchDTO.getVerificationStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("verificationStatus"), searchDTO.getVerificationStatus()));
            }
            if (searchDTO.getFeatured() != null) {
                predicates.add(criteriaBuilder.equal(root.get("featured"), searchDTO.getFeatured()));
            }
            if (searchDTO.getActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), searchDTO.getActive()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteHotel(Long id) {
        log.info("Deleting hotel with ID: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hôtel non trouvé"));
        hotel.setActive(false);
        hotelRepository.save(hotel);
        log.info("Hotel deactivated successfully");
    }
    
    private HotelResponseDTO mapToResponseDTO(Hotel hotel) {
        return HotelResponseDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(hotel.getAddress())
                .city(hotel.getCity())
                .region(hotel.getRegion())
                .latitude(hotel.getLatitude())
                .longitude(hotel.getLongitude())
                .email(hotel.getEmail())
                .phone(hotel.getPhone())
                .website(hotel.getWebsite())
                .category(hotel.getCategory())
                .starRating(hotel.getStarRating())
                .totalRooms(hotel.getTotalRooms())
                .priceRangeMin(hotel.getPriceRangeMin())
                .priceRangeMax(hotel.getPriceRangeMax())
                .currency(hotel.getCurrency())
                .amenities(hotel.getAmenities())
                .spokenLanguages(hotel.getSpokenLanguages())
                .photoUrls(hotel.getPhotoUrls())
                .culturalHighlights(hotel.getCulturalHighlights())
                .sustainabilityPractices(hotel.getSustainabilityPractices())
                .checkInTime(hotel.getCheckInTime())
                .checkOutTime(hotel.getCheckOutTime())
                .partnershipStatus(hotel.getPartnershipStatus())
                .verificationStatus(hotel.getVerificationStatus())
                .specialOffers(hotel.getSpecialOffers())
                .averageRating(hotel.getAverageRating())
                .totalReviews(hotel.getTotalReviews())
                .viewCount(hotel.getViewCount())
                .bookingCount(hotel.getBookingCount())
                .featured(hotel.isFeatured())
                .active(hotel.isActive())
                .createdAt(hotel.getCreatedAt())
                .updatedAt(hotel.getUpdatedAt())
                .build();
    }
}