package sn.discover.discoversenegal.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.discover.discoversenegal.dto.*;
import sn.discover.discoversenegal.entities.*;
import sn.discover.discoversenegal.repositories.GuideRepository;
import sn.discover.discoversenegal.repositories.UserRepository;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuideService {
    
    private final GuideRepository guideRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public GuideResponseDTO createGuide(GuideCreateDTO dto) {
        log.info("Creating new guide: {} {}", dto.getFirstName(), dto.getLastName());
        
        if (dto.getEmail() != null && guideRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Un guide avec cet email existe déjà");
        }
        
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        Guide guide = Guide.builder()
                .user(user)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .nationality(dto.getNationality())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .whatsapp(dto.getWhatsapp())
                .photoUrl(dto.getPhotoUrl())
                .city(dto.getCity())
                .region(dto.getRegion())
                .coverageAreas(dto.getCoverageAreas())
                .languages(dto.getLanguages())
                .primaryLanguage(dto.getPrimaryLanguage())
                .specialties(dto.getSpecialties())
                .guideType(dto.getGuideType())
                .yearsOfExperience(dto.getYearsOfExperience())
                .startedGuidingDate(dto.getStartedGuidingDate())
                .biography(dto.getBiography())
                .expertise(dto.getExpertise())
                .certifications(dto.getCertifications())
                .isOfficiallyLicensed(dto.getIsOfficiallyLicensed() != null ? dto.getIsOfficiallyLicensed() : false)
                .licenseNumber(dto.getLicenseNumber())
                .licenseExpiryDate(dto.getLicenseExpiryDate())
                .qualifications(dto.getQualifications())
                .serviceType(dto.getServiceType())
                .maxGroupSize(dto.getMaxGroupSize())
                .offersPrivateTours(dto.getOffersPrivateTours() != null ? dto.getOffersPrivateTours() : false)
                .offersGroupTours(dto.getOffersGroupTours() != null ? dto.getOffersGroupTours() : false)
                .offersMultiDayTours(dto.getOffersMultiDayTours() != null ? dto.getOffersMultiDayTours() : false)
                .providesTransportation(dto.getProvidesTransportation() != null ? dto.getProvidesTransportation() : false)
                .transportationType(dto.getTransportationType())
                .providesAccommodationBooking(dto.getProvidesAccommodationBooking() != null ? dto.getProvidesAccommodationBooking() : false)
                .offersCustomItineraries(dto.getOffersCustomItineraries() != null ? dto.getOffersCustomItineraries() : false)
                .hourlyRate(dto.getHourlyRate())
                .halfDayRate(dto.getHalfDayRate())
                .fullDayRate(dto.getFullDayRate())
                .multiDayRate(dto.getMultiDayRate())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "XOF")
                .pricingNotes(dto.getPricingNotes())
                .availabilityStatus(dto.getAvailabilityStatus() != null ? dto.getAvailabilityStatus() : AvailabilityStatus.AVAILABLE)
                .availableDays(dto.getAvailableDays())
                .availableWeekends(dto.getAvailableWeekends() != null ? dto.getAvailableWeekends() : true)
                .availableHolidays(dto.getAvailableHolidays() != null ? dto.getAvailableHolidays() : true)
                .advanceBookingDays(dto.getAdvanceBookingDays())
                .equipmentProvided(dto.getEquipmentProvided())
                .availableForOlympics(dto.getAvailableForOlympics() != null ? dto.getAvailableForOlympics() : false)
                .olympicExperience(dto.getOlympicExperience())
                .olympicSpecialties(dto.getOlympicSpecialties())
                .firstAidCertified(dto.getFirstAidCertified() != null ? dto.getFirstAidCertified() : false)
                .websiteUrl(dto.getWebsiteUrl())
                .facebookUrl(dto.getFacebookUrl())
                .instagramUrl(dto.getInstagramUrl())
                .linkedinUrl(dto.getLinkedinUrl())
                .verificationStatus(VerificationStatus.PENDING)
                .partnershipStatus(PartnershipStatus.PROSPECT)
                .active(true)
                .acceptingBookings(true)
                .build();
        
        Guide savedGuide = guideRepository.save(guide);
        log.info("Guide created successfully with ID: {}", savedGuide.getId());
        
        return mapToResponseDTO(savedGuide);
    }
    
    @Transactional
    public GuideResponseDTO updateGuide(Long id, GuideUpdateDTO dto) {
        log.info("Updating guide with ID: {}", id);
        
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide non trouvé"));
        
        if (dto.getFirstName() != null) guide.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) guide.setLastName(dto.getLastName());
        if (dto.getPhone() != null) guide.setPhone(dto.getPhone());
        if (dto.getWhatsapp() != null) guide.setWhatsapp(dto.getWhatsapp());
        if (dto.getPhotoUrl() != null) guide.setPhotoUrl(dto.getPhotoUrl());
        if (dto.getCity() != null) guide.setCity(dto.getCity());
        if (dto.getRegion() != null) guide.setRegion(dto.getRegion());
        if (dto.getCoverageAreas() != null) guide.setCoverageAreas(dto.getCoverageAreas());
        if (dto.getLanguages() != null) guide.setLanguages(dto.getLanguages());
        if (dto.getSpecialties() != null) guide.setSpecialties(dto.getSpecialties());
        if (dto.getGuideType() != null) guide.setGuideType(dto.getGuideType());
        if (dto.getBiography() != null) guide.setBiography(dto.getBiography());
        if (dto.getExpertise() != null) guide.setExpertise(dto.getExpertise());
        if (dto.getCertifications() != null) guide.setCertifications(dto.getCertifications());
        if (dto.getQualifications() != null) guide.setQualifications(dto.getQualifications());
        if (dto.getServiceType() != null) guide.setServiceType(dto.getServiceType());
        if (dto.getMaxGroupSize() != null) guide.setMaxGroupSize(dto.getMaxGroupSize());
        if (dto.getOffersPrivateTours() != null) guide.setOffersPrivateTours(dto.getOffersPrivateTours());
        if (dto.getOffersGroupTours() != null) guide.setOffersGroupTours(dto.getOffersGroupTours());
        if (dto.getOffersMultiDayTours() != null) guide.setOffersMultiDayTours(dto.getOffersMultiDayTours());
        if (dto.getProvidesTransportation() != null) guide.setProvidesTransportation(dto.getProvidesTransportation());
        if (dto.getTransportationType() != null) guide.setTransportationType(dto.getTransportationType());
        if (dto.getProvidesAccommodationBooking() != null) guide.setProvidesAccommodationBooking(dto.getProvidesAccommodationBooking());
        if (dto.getOffersCustomItineraries() != null) guide.setOffersCustomItineraries(dto.getOffersCustomItineraries());
        if (dto.getHourlyRate() != null) guide.setHourlyRate(dto.getHourlyRate());
        if (dto.getHalfDayRate() != null) guide.setHalfDayRate(dto.getHalfDayRate());
        if (dto.getFullDayRate() != null) guide.setFullDayRate(dto.getFullDayRate());
        if (dto.getMultiDayRate() != null) guide.setMultiDayRate(dto.getMultiDayRate());
        if (dto.getPricingNotes() != null) guide.setPricingNotes(dto.getPricingNotes());
        if (dto.getAvailabilityStatus() != null) guide.setAvailabilityStatus(dto.getAvailabilityStatus());
        if (dto.getAvailableDays() != null) guide.setAvailableDays(dto.getAvailableDays());
        if (dto.getAvailableWeekends() != null) guide.setAvailableWeekends(dto.getAvailableWeekends());
        if (dto.getAvailableHolidays() != null) guide.setAvailableHolidays(dto.getAvailableHolidays());
        if (dto.getAdvanceBookingDays() != null) guide.setAdvanceBookingDays(dto.getAdvanceBookingDays());
        if (dto.getEquipmentProvided() != null) guide.setEquipmentProvided(dto.getEquipmentProvided());
        if (dto.getAvailableForOlympics() != null) guide.setAvailableForOlympics(dto.getAvailableForOlympics());
        if (dto.getOlympicExperience() != null) guide.setOlympicExperience(dto.getOlympicExperience());
        if (dto.getOlympicSpecialties() != null) guide.setOlympicSpecialties(dto.getOlympicSpecialties());
        if (dto.getWebsiteUrl() != null) guide.setWebsiteUrl(dto.getWebsiteUrl());
        if (dto.getFacebookUrl() != null) guide.setFacebookUrl(dto.getFacebookUrl());
        if (dto.getInstagramUrl() != null) guide.setInstagramUrl(dto.getInstagramUrl());
        if (dto.getLinkedinUrl() != null) guide.setLinkedinUrl(dto.getLinkedinUrl());
        if (dto.getFeatured() != null) guide.setFeatured(dto.getFeatured());
        if (dto.getActive() != null) guide.setActive(dto.getActive());
        if (dto.getAcceptingBookings() != null) guide.setAcceptingBookings(dto.getAcceptingBookings());
        
        Guide updatedGuide = guideRepository.save(guide);
        log.info("Guide updated successfully");
        
        return mapToResponseDTO(updatedGuide);
    }
    
    @Transactional
    public GuideResponseDTO updatePartnership(Long id, GuidePartnershipUpdateDTO dto) {
        log.info("Updating partnership for guide ID: {}", id);
        
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide non trouvé"));
        
        if (dto.getPartnershipStatus() != null) {
            guide.setPartnershipStatus(dto.getPartnershipStatus());
        }
        if (dto.getPartnershipStartDate() != null) {
            guide.setPartnershipStartDate(dto.getPartnershipStartDate());
        }
        if (dto.getPartnershipEndDate() != null) {
            guide.setPartnershipEndDate(dto.getPartnershipEndDate());
        }
        if (dto.getCommissionRate() != null) {
            guide.setCommissionRate(dto.getCommissionRate());
        }
        if (dto.getSpecialOffersJOJ() != null) {
            guide.setSpecialOffersJOJ(dto.getSpecialOffersJOJ());
        }
        
        Guide updatedGuide = guideRepository.save(guide);
        log.info("Partnership updated successfully");
        
        return mapToResponseDTO(updatedGuide);
    }
    
    @Transactional
    public GuideResponseDTO updateVerification(Long id, GuideVerificationUpdateDTO dto) {
        log.info("Updating verification status for guide ID: {}", id);
        
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide non trouvé"));
        
        guide.setVerificationStatus(dto.getVerificationStatus());
        guide.setLastVerifiedAt(LocalDateTime.now());
        
        if (dto.getBackgroundCheckCompleted() != null) {
            guide.setBackgroundCheckCompleted(dto.getBackgroundCheckCompleted());
        }
        if (dto.getBackgroundCheckDate() != null) {
            guide.setBackgroundCheckDate(dto.getBackgroundCheckDate());
        }
        
        Guide updatedGuide = guideRepository.save(guide);
        log.info("Verification status updated to: {}", dto.getVerificationStatus());
        
        return mapToResponseDTO(updatedGuide);
    }
    
    @Transactional
    public GuideResponseDTO addReview(Long id, GuideReviewDTO dto) {
        log.info("Adding review for guide ID: {}", id);
        
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide non trouvé"));
        
        int currentReviews = guide.getTotalReviews() != null ? guide.getTotalReviews() : 0;
        
        // Calcul moyenne globale
        double currentAverage = guide.getAverageRating() != null ? guide.getAverageRating() : 0.0;
        double newAverage = ((currentAverage * currentReviews) + dto.getOverallRating()) / (currentReviews + 1);
        guide.setAverageRating(newAverage);
        
        // Calcul moyennes détaillées
        if (dto.getProfessionalismRating() != null) {
            double current = guide.getProfessionalismRating() != null ? guide.getProfessionalismRating() : 0.0;
            guide.setProfessionalismRating(((current * currentReviews) + dto.getProfessionalismRating()) / (currentReviews + 1));
        }
        
        if (dto.getKnowledgeRating() != null) {
            double current = guide.getKnowledgeRating() != null ? guide.getKnowledgeRating() : 0.0;
            guide.setKnowledgeRating(((current * currentReviews) + dto.getKnowledgeRating()) / (currentReviews + 1));
        }
        
        if (dto.getCommunicationRating() != null) {
            double current = guide.getCommunicationRating() != null ? guide.getCommunicationRating() : 0.0;
            guide.setCommunicationRating(((current * currentReviews) + dto.getCommunicationRating()) / (currentReviews + 1));
        }
        
        if (dto.getPunctualityRating() != null) {
            double current = guide.getPunctualityRating() != null ? guide.getPunctualityRating() : 0.0;
            guide.setPunctualityRating(((current * currentReviews) + dto.getPunctualityRating()) / (currentReviews + 1));
        }
        
        guide.setTotalReviews(currentReviews + 1);
        
        Guide updatedGuide = guideRepository.save(guide);
        log.info("Review added successfully");
        
        return mapToResponseDTO(updatedGuide);
    }
    
    @Transactional
    public GuideResponseDTO incrementViewCount(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide non trouvé"));
        guide.setViewCount(guide.getViewCount() + 1);
        return mapToResponseDTO(guideRepository.save(guide));
    }
    
    @Transactional
    public GuideResponseDTO incrementBookingCount(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide non trouvé"));
        guide.setBookingCount(guide.getBookingCount() + 1);
        guide.setTotalTours(guide.getTotalTours() + 1);
        return mapToResponseDTO(guideRepository.save(guide));
    }
    
    @Transactional
    public GuideResponseDTO incrementFavoriteCount(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide non trouvé"));
        guide.setFavoriteCount(guide.getFavoriteCount() + 1);
        return mapToResponseDTO(guideRepository.save(guide));
    }
    
    @Transactional(readOnly = true)
    public GuideResponseDTO getGuideById(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide non trouvé"));
        return mapToResponseDTO(guide);
    }
    
    @Transactional(readOnly = true)
    public List<GuideResponseDTO> getAllGuides() {
        return guideRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<GuideResponseDTO> getGuidesByCity(String city) {
        return guideRepository.findByCityAndActiveTrue(city).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<GuideResponseDTO> getGuidesByType(GuideType type) {
        return guideRepository.findByGuideTypeAndActiveTrue(type).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<GuideResponseDTO> getGuidesByLanguages(List<String> languages) {
        return guideRepository.findByLanguages(languages).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<GuideResponseDTO> getFeaturedGuides() {
        return guideRepository.findByFeaturedTrueAndActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<GuideResponseDTO> getTopRatedGuides() {
        return guideRepository.findTopRated().stream()
                .limit(10)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<GuideResponseDTO> getMostPopularGuides() {
        return guideRepository.findMostPopular().stream()
                .limit(10)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<GuideResponseDTO> getActivePartners() {
        return guideRepository.findActivePartners().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<GuideResponseDTO> getAvailableGuides() {
        return guideRepository.findCurrentlyAvailable().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<GuideResponseDTO> searchGuides(GuideSearchDTO searchDTO) {
        return guideRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (searchDTO.getCity() != null) {
                predicates.add(criteriaBuilder.equal(root.get("city"), searchDTO.getCity()));
            }
            if (searchDTO.getRegion() != null) {
                predicates.add(criteriaBuilder.equal(root.get("region"), searchDTO.getRegion()));
            }
            if (searchDTO.getGuideType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("guideType"), searchDTO.getGuideType()));
            }
            if (searchDTO.getServiceType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("serviceType"), searchDTO.getServiceType()));
            }
            if (searchDTO.getOffersPrivateTours() != null) {
                predicates.add(criteriaBuilder.equal(root.get("offersPrivateTours"), searchDTO.getOffersPrivateTours()));
            }
            if (searchDTO.getOffersGroupTours() != null) {
                predicates.add(criteriaBuilder.equal(root.get("offersGroupTours"), searchDTO.getOffersGroupTours()));
            }
            if (searchDTO.getOffersMultiDayTours() != null) {
                predicates.add(criteriaBuilder.equal(root.get("offersMultiDayTours"), searchDTO.getOffersMultiDayTours()));
            }
            if (searchDTO.getProvidesTransportation() != null) {
                predicates.add(criteriaBuilder.equal(root.get("providesTransportation"), searchDTO.getProvidesTransportation()));
            }
            if (searchDTO.getOffersCustomItineraries() != null) {
                predicates.add(criteriaBuilder.equal(root.get("offersCustomItineraries"), searchDTO.getOffersCustomItineraries()));
            }
            if (searchDTO.getAvailableForOlympics() != null) {
                predicates.add(criteriaBuilder.equal(root.get("availableForOlympics"), searchDTO.getAvailableForOlympics()));
            }
            if (searchDTO.getAvailabilityStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("availabilityStatus"), searchDTO.getAvailabilityStatus()));
            }
            if (searchDTO.getIsOfficiallyLicensed() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isOfficiallyLicensed"), searchDTO.getIsOfficiallyLicensed()));
            }
            if (searchDTO.getMinRating() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("averageRating"), searchDTO.getMinRating()));
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
            if (searchDTO.getAcceptingBookings() != null) {
                predicates.add(criteriaBuilder.equal(root.get("acceptingBookings"), searchDTO.getAcceptingBookings()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteGuide(Long id) {
        log.info("Deleting guide with ID: {}", id);
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide non trouvé"));
        guide.setActive(false);
        guideRepository.save(guide);
        log.info("Guide deactivated successfully");
    }
    
    private GuideResponseDTO mapToResponseDTO(Guide guide) {
        return GuideResponseDTO.builder()
                .id(guide.getId())
                .firstName(guide.getFirstName())
                .lastName(guide.getLastName())
                .gender(guide.getGender())
                .nationality(guide.getNationality())
                .email(guide.getEmail())
                .phone(guide.getPhone())
                .whatsapp(guide.getWhatsapp())
                .photoUrl(guide.getPhotoUrl())
                .city(guide.getCity())
                .region(guide.getRegion())
                .coverageAreas(guide.getCoverageAreas())
                .languages(guide.getLanguages())
                .primaryLanguage(guide.getPrimaryLanguage())
                .specialties(guide.getSpecialties())
                .guideType(guide.getGuideType())
                .yearsOfExperience(guide.getYearsOfExperience())
                .biography(guide.getBiography())
                .expertise(guide.getExpertise())
                .certifications(guide.getCertifications())
                .isOfficiallyLicensed(guide.getIsOfficiallyLicensed())
                .licenseNumber(guide.getLicenseNumber())
                .qualifications(guide.getQualifications())
                .serviceType(guide.getServiceType())
                .maxGroupSize(guide.getMaxGroupSize())
                .offersPrivateTours(guide.getOffersPrivateTours())
                .offersGroupTours(guide.getOffersGroupTours())
                .offersMultiDayTours(guide.getOffersMultiDayTours())
                .providesTransportation(guide.getProvidesTransportation())
                .transportationType(guide.getTransportationType())
                .providesAccommodationBooking(guide.getProvidesAccommodationBooking())
                .offersCustomItineraries(guide.getOffersCustomItineraries())
                .hourlyRate(guide.getHourlyRate())
                .halfDayRate(guide.getHalfDayRate())
                .fullDayRate(guide.getFullDayRate())
                .multiDayRate(guide.getMultiDayRate())
                .currency(guide.getCurrency())
                .pricingNotes(guide.getPricingNotes())
                .availabilityStatus(guide.getAvailabilityStatus())
                .availableDays(guide.getAvailableDays())
                .availableWeekends(guide.getAvailableWeekends())
                .availableHolidays(guide.getAvailableHolidays())
                .advanceBookingDays(guide.getAdvanceBookingDays())
                .equipmentProvided(guide.getEquipmentProvided())
                .availableForOlympics(guide.getAvailableForOlympics())
                .olympicExperience(guide.getOlympicExperience())
                .olympicSpecialties(guide.getOlympicSpecialties())
                .partnershipStatus(guide.getPartnershipStatus())
                .specialOffersJOJ(guide.getSpecialOffersJOJ())
                .verificationStatus(guide.getVerificationStatus())
                .backgroundCheckCompleted(guide.getBackgroundCheckCompleted())
                .firstAidCertified(guide.getFirstAidCertified())
                .averageRating(guide.getAverageRating())
                .totalReviews(guide.getTotalReviews())
                .professionalismRating(guide.getProfessionalismRating())
                .knowledgeRating(guide.getKnowledgeRating())
                .communicationRating(guide.getCommunicationRating())
                .punctualityRating(guide.getPunctualityRating())
                .totalTours(guide.getTotalTours())
                .totalClients(guide.getTotalClients())
                .viewCount(guide.getViewCount())
                .bookingCount(guide.getBookingCount())
                .favoriteCount(guide.getFavoriteCount())
                .websiteUrl(guide.getWebsiteUrl())
                .facebookUrl(guide.getFacebookUrl())
                .instagramUrl(guide.getInstagramUrl())
                .linkedinUrl(guide.getLinkedinUrl())
                .featured(guide.getFeatured())
                .active(guide.getActive())
                .acceptingBookings(guide.getAcceptingBookings())
                .createdAt(guide.getCreatedAt())
                .updatedAt(guide.getUpdatedAt())
                .build();
    }
}
