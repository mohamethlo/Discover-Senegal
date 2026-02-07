package sn.discover.discoversenegal.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.discover.discoversenegal.dto.*;
import sn.discover.discoversenegal.entities.*;
import sn.discover.discoversenegal.repositories.RestaurantRepository;
import sn.discover.discoversenegal.repositories.UserRepository;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public RestaurantResponseDTO createRestaurant(RestaurantCreateDTO dto) {
        log.info("Creating new restaurant: {}", dto.getName());
        
        if (dto.getEmail() != null && restaurantRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Un restaurant avec cet email existe déjà");
        }
        
        User owner = null;
        if (dto.getOwnerId() != null) {
            owner = userRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Propriétaire non trouvé"));
        }
        
        Restaurant restaurant = Restaurant.builder()
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
                .type(dto.getType())
                .cuisineTypes(dto.getCuisineTypes())
                .specialties(dto.getSpecialties())
                .priceRange(dto.getPriceRange())
                .averagePricePerPerson(dto.getAveragePricePerPerson())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "XOF")
                .seatingCapacity(dto.getSeatingCapacity())
                .acceptsReservations(dto.getAcceptsReservations())
                .openingHours(mapToOpeningHours(dto.getOpeningHours()))
                .services(dto.getServices())
                .paymentMethods(dto.getPaymentMethods())
                .spokenLanguages(dto.getSpokenLanguages())
                .photoUrls(dto.getPhotoUrls())
                .menuUrl(dto.getMenuUrl())
                .menuDescription(dto.getMenuDescription())
                .hasVegetarianOptions(dto.getHasVegetarianOptions() != null ? dto.getHasVegetarianOptions() : false)
                .hasVeganOptions(dto.getHasVeganOptions() != null ? dto.getHasVeganOptions() : false)
                .hasHalalOptions(dto.getHasHalalOptions() != null ? dto.getHasHalalOptions() : false)
                .hasGlutenFreeOptions(dto.getHasGlutenFreeOptions() != null ? dto.getHasGlutenFreeOptions() : false)
                .culturalSignificance(dto.getCulturalSignificance())
                .chefStory(dto.getChefStory())
                .localIngredients(dto.getLocalIngredients())
                .ambiance(dto.getAmbiance())
                .hasLiveMusic(dto.getHasLiveMusic() != null ? dto.getHasLiveMusic() : false)
                .hasOutdoorSeating(dto.getHasOutdoorSeating() != null ? dto.getHasOutdoorSeating() : false)
                .hasPrivateDining(dto.getHasPrivateDining() != null ? dto.getHasPrivateDining() : false)
                .cateringAvailable(dto.getCateringAvailable() != null ? dto.getCateringAvailable() : false)
                .maxCateringCapacity(dto.getMaxCateringCapacity())
                .hasHygieneRating(dto.getHasHygieneRating() != null ? dto.getHasHygieneRating() : false)
                .hygieneCertificate(dto.getHygieneCertificate())
                .isHalalCertified(dto.getIsHalalCertified() != null ? dto.getIsHalalCertified() : false)
                .owner(owner)
                .verificationStatus(VerificationStatus.PENDING)
                .partnershipStatus(PartnershipStatus.PROSPECT)
                .active(true)
                .build();
        
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        log.info("Restaurant created successfully with ID: {}", savedRestaurant.getId());
        
        return mapToResponseDTO(savedRestaurant);
    }
    
    @Transactional
    public RestaurantResponseDTO updateRestaurant(Long id, RestaurantUpdateDTO dto) {
        log.info("Updating restaurant with ID: {}", id);
        
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé"));
        
        if (dto.getName() != null) restaurant.setName(dto.getName());
        if (dto.getDescription() != null) restaurant.setDescription(dto.getDescription());
        if (dto.getAddress() != null) restaurant.setAddress(dto.getAddress());
        if (dto.getCity() != null) restaurant.setCity(dto.getCity());
        if (dto.getRegion() != null) restaurant.setRegion(dto.getRegion());
        if (dto.getPostalCode() != null) restaurant.setPostalCode(dto.getPostalCode());
        if (dto.getLatitude() != null) restaurant.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) restaurant.setLongitude(dto.getLongitude());
        if (dto.getEmail() != null) restaurant.setEmail(dto.getEmail());
        if (dto.getPhone() != null) restaurant.setPhone(dto.getPhone());
        if (dto.getWebsite() != null) restaurant.setWebsite(dto.getWebsite());
        if (dto.getType() != null) restaurant.setType(dto.getType());
        if (dto.getCuisineTypes() != null) restaurant.setCuisineTypes(dto.getCuisineTypes());
        if (dto.getSpecialties() != null) restaurant.setSpecialties(dto.getSpecialties());
        if (dto.getPriceRange() != null) restaurant.setPriceRange(dto.getPriceRange());
        if (dto.getAveragePricePerPerson() != null) restaurant.setAveragePricePerPerson(dto.getAveragePricePerPerson());
        if (dto.getSeatingCapacity() != null) restaurant.setSeatingCapacity(dto.getSeatingCapacity());
        if (dto.getAcceptsReservations() != null) restaurant.setAcceptsReservations(dto.getAcceptsReservations());
        if (dto.getOpeningHours() != null) restaurant.setOpeningHours(mapToOpeningHours(dto.getOpeningHours()));
        if (dto.getServices() != null) restaurant.setServices(dto.getServices());
        if (dto.getPaymentMethods() != null) restaurant.setPaymentMethods(dto.getPaymentMethods());
        if (dto.getSpokenLanguages() != null) restaurant.setSpokenLanguages(dto.getSpokenLanguages());
        if (dto.getPhotoUrls() != null) restaurant.setPhotoUrls(dto.getPhotoUrls());
        if (dto.getMenuUrl() != null) restaurant.setMenuUrl(dto.getMenuUrl());
        if (dto.getMenuDescription() != null) restaurant.setMenuDescription(dto.getMenuDescription());
        if (dto.getHasVegetarianOptions() != null) restaurant.setHasVegetarianOptions(dto.getHasVegetarianOptions());
        if (dto.getHasVeganOptions() != null) restaurant.setHasVeganOptions(dto.getHasVeganOptions());
        if (dto.getHasHalalOptions() != null) restaurant.setHasHalalOptions(dto.getHasHalalOptions());
        if (dto.getHasGlutenFreeOptions() != null) restaurant.setHasGlutenFreeOptions(dto.getHasGlutenFreeOptions());
        if (dto.getCulturalSignificance() != null) restaurant.setCulturalSignificance(dto.getCulturalSignificance());
        if (dto.getChefStory() != null) restaurant.setChefStory(dto.getChefStory());
        if (dto.getLocalIngredients() != null) restaurant.setLocalIngredients(dto.getLocalIngredients());
        if (dto.getAmbiance() != null) restaurant.setAmbiance(dto.getAmbiance());
        if (dto.getHasLiveMusic() != null) restaurant.setHasLiveMusic(dto.getHasLiveMusic());
        if (dto.getHasOutdoorSeating() != null) restaurant.setHasOutdoorSeating(dto.getHasOutdoorSeating());
        if (dto.getHasPrivateDining() != null) restaurant.setHasPrivateDining(dto.getHasPrivateDining());
        if (dto.getCateringAvailable() != null) restaurant.setCateringAvailable(dto.getCateringAvailable());
        if (dto.getMaxCateringCapacity() != null) restaurant.setMaxCateringCapacity(dto.getMaxCateringCapacity());
        if (dto.getFeatured() != null) restaurant.setFeatured(dto.getFeatured());
        if (dto.getActive() != null) restaurant.setActive(dto.getActive());
        
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        log.info("Restaurant updated successfully");
        
        return mapToResponseDTO(updatedRestaurant);
    }
    
    @Transactional
    public RestaurantResponseDTO updatePartnership(Long id, RestaurantPartnershipUpdateDTO dto) {
        log.info("Updating partnership for restaurant ID: {}", id);
        
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé"));
        
        if (dto.getPartnershipStatus() != null) {
            restaurant.setPartnershipStatus(dto.getPartnershipStatus());
        }
        if (dto.getPartnershipStartDate() != null) {
            restaurant.setPartnershipStartDate(dto.getPartnershipStartDate());
        }
        if (dto.getPartnershipEndDate() != null) {
            restaurant.setPartnershipEndDate(dto.getPartnershipEndDate());
        }
        if (dto.getCommissionRate() != null) {
            restaurant.setCommissionRate(dto.getCommissionRate());
        }
        if (dto.getSpecialOffersJOJ() != null) {
            restaurant.setSpecialOffersJOJ(dto.getSpecialOffersJOJ());
        }
        
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        log.info("Partnership updated successfully");
        
        return mapToResponseDTO(updatedRestaurant);
    }
    
    @Transactional
    public RestaurantResponseDTO updateVerification(Long id, RestaurantVerificationUpdateDTO dto) {
        log.info("Updating verification status for restaurant ID: {}", id);
        
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé"));
        
        restaurant.setVerificationStatus(dto.getVerificationStatus());
        restaurant.setLastVerifiedAt(LocalDateTime.now());
        
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        log.info("Verification status updated to: {}", dto.getVerificationStatus());
        
        return mapToResponseDTO(updatedRestaurant);
    }
    
    @Transactional
    public RestaurantResponseDTO addReview(Long id, RestaurantReviewDTO dto) {
        log.info("Adding review for restaurant ID: {}", id);
        
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé"));
        
        // Mise à jour des notes
        int currentReviews = restaurant.getTotalReviews() != null ? restaurant.getTotalReviews() : 0;
        
        // Calcul moyenne globale
        double currentAverage = restaurant.getAverageRating() != null ? restaurant.getAverageRating() : 0.0;
        double newAverage = ((currentAverage * currentReviews) + dto.getOverallRating()) / (currentReviews + 1);
        restaurant.setAverageRating(newAverage);
        
        // Calcul moyennes détaillées
        if (dto.getFoodQualityRating() != null) {
            double currentFood = restaurant.getFoodQualityRating() != null ? restaurant.getFoodQualityRating() : 0.0;
            restaurant.setFoodQualityRating(((currentFood * currentReviews) + dto.getFoodQualityRating()) / (currentReviews + 1));
        }
        
        if (dto.getServiceRating() != null) {
            double currentService = restaurant.getServiceRating() != null ? restaurant.getServiceRating() : 0.0;
            restaurant.setServiceRating(((currentService * currentReviews) + dto.getServiceRating()) / (currentReviews + 1));
        }
        
        if (dto.getAmbianceRating() != null) {
            double currentAmbiance = restaurant.getAmbianceRating() != null ? restaurant.getAmbianceRating() : 0.0;
            restaurant.setAmbianceRating(((currentAmbiance * currentReviews) + dto.getAmbianceRating()) / (currentReviews + 1));
        }
        
        if (dto.getValueForMoneyRating() != null) {
            double currentValue = restaurant.getValueForMoneyRating() != null ? restaurant.getValueForMoneyRating() : 0.0;
            restaurant.setValueForMoneyRating(((currentValue * currentReviews) + dto.getValueForMoneyRating()) / (currentReviews + 1));
        }
        
        restaurant.setTotalReviews(currentReviews + 1);
        
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        log.info("Review added successfully");
        
        return mapToResponseDTO(updatedRestaurant);
    }
    
    @Transactional
    public RestaurantResponseDTO incrementViewCount(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé"));
        restaurant.setViewCount(restaurant.getViewCount() + 1);
        return mapToResponseDTO(restaurantRepository.save(restaurant));
    }
    
    @Transactional
    public RestaurantResponseDTO incrementReservationCount(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé"));
        restaurant.setReservationCount(restaurant.getReservationCount() + 1);
        return mapToResponseDTO(restaurantRepository.save(restaurant));
    }
    
    @Transactional
    public RestaurantResponseDTO incrementFavoriteCount(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé"));
        restaurant.setFavoriteCount(restaurant.getFavoriteCount() + 1);
        return mapToResponseDTO(restaurantRepository.save(restaurant));
    }
    
    @Transactional(readOnly = true)
    public RestaurantResponseDTO getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé"));
        return mapToResponseDTO(restaurant);
    }
    
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getAllRestaurants() {
        return restaurantRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getRestaurantsByCity(String city) {
        return restaurantRepository.findByCityAndActiveTrue(city).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getRestaurantsByType(RestaurantType type) {
        return restaurantRepository.findByTypeAndActiveTrue(type).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getRestaurantsByCuisine(List<String> cuisineTypes) {
        return restaurantRepository.findByCuisineTypes(cuisineTypes).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getFeaturedRestaurants() {
        return restaurantRepository.findByFeaturedTrueAndActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getTopRatedRestaurants() {
        return restaurantRepository.findTopRated().stream()
                .limit(10)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getMostPopularRestaurants() {
        return restaurantRepository.findMostPopular().stream()
                .limit(10)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getActivePartners() {
        return restaurantRepository.findActivePartners().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> searchRestaurants(RestaurantSearchDTO searchDTO) {
        return restaurantRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (searchDTO.getCity() != null) {
                predicates.add(criteriaBuilder.equal(root.get("city"), searchDTO.getCity()));
            }
            if (searchDTO.getRegion() != null) {
                predicates.add(criteriaBuilder.equal(root.get("region"), searchDTO.getRegion()));
            }
            if (searchDTO.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), searchDTO.getType()));
            }
            if (searchDTO.getPriceRange() != null) {
                predicates.add(criteriaBuilder.equal(root.get("priceRange"), searchDTO.getPriceRange()));
            }
            if (searchDTO.getAmbiance() != null) {
                predicates.add(criteriaBuilder.equal(root.get("ambiance"), searchDTO.getAmbiance()));
            }
            if (searchDTO.getHasVegetarianOptions() != null) {
                predicates.add(criteriaBuilder.equal(root.get("hasVegetarianOptions"), searchDTO.getHasVegetarianOptions()));
            }
            if (searchDTO.getHasVeganOptions() != null) {
                predicates.add(criteriaBuilder.equal(root.get("hasVeganOptions"), searchDTO.getHasVeganOptions()));
            }
            if (searchDTO.getHasHalalOptions() != null) {
                predicates.add(criteriaBuilder.equal(root.get("hasHalalOptions"), searchDTO.getHasHalalOptions()));
            }
            if (searchDTO.getAcceptsReservations() != null) {
                predicates.add(criteriaBuilder.equal(root.get("acceptsReservations"), searchDTO.getAcceptsReservations()));
            }
            if (searchDTO.getHasOutdoorSeating() != null) {
                predicates.add(criteriaBuilder.equal(root.get("hasOutdoorSeating"), searchDTO.getHasOutdoorSeating()));
            }
            if (searchDTO.getHasLiveMusic() != null) {
                predicates.add(criteriaBuilder.equal(root.get("hasLiveMusic"), searchDTO.getHasLiveMusic()));
            }
            if (searchDTO.getCateringAvailable() != null) {
                predicates.add(criteriaBuilder.equal(root.get("cateringAvailable"), searchDTO.getCateringAvailable()));
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
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteRestaurant(Long id) {
        log.info("Deleting restaurant with ID: {}", id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé"));
        restaurant.setActive(false);
        restaurantRepository.save(restaurant);
        log.info("Restaurant deactivated successfully");
    }
    
    private OpeningHours mapToOpeningHours(OpeningHoursDTO dto) {
        if (dto == null) return null;
        
        OpeningHours hours = new OpeningHours();
        hours.setMondayOpen(dto.getMondayOpen());
        hours.setMondayClose(dto.getMondayClose());
        hours.setTuesdayOpen(dto.getTuesdayOpen());
        hours.setTuesdayClose(dto.getTuesdayClose());
        hours.setWednesdayOpen(dto.getWednesdayOpen());
        hours.setWednesdayClose(dto.getWednesdayClose());
        hours.setThursdayOpen(dto.getThursdayOpen());
        hours.setThursdayClose(dto.getThursdayClose());
        hours.setFridayOpen(dto.getFridayOpen());
        hours.setFridayClose(dto.getFridayClose());
        hours.setSaturdayOpen(dto.getSaturdayOpen());
        hours.setSaturdayClose(dto.getSaturdayClose());
        hours.setSundayOpen(dto.getSundayOpen());
        hours.setSundayClose(dto.getSundayClose());
        hours.setClosedMonday(dto.getClosedMonday());
        hours.setClosedTuesday(dto.getClosedTuesday());
        hours.setClosedWednesday(dto.getClosedWednesday());
        hours.setClosedThursday(dto.getClosedThursday());
        hours.setClosedFriday(dto.getClosedFriday());
        hours.setClosedSaturday(dto.getClosedSaturday());
        hours.setClosedSunday(dto.getClosedSunday());
        
        return hours;
    }
    
    private OpeningHoursDTO mapToOpeningHoursDTO(OpeningHours hours) {
        if (hours == null) return null;
        
        return OpeningHoursDTO.builder()
                .mondayOpen(hours.getMondayOpen())
                .mondayClose(hours.getMondayClose())
                .tuesdayOpen(hours.getTuesdayOpen())
                .tuesdayClose(hours.getTuesdayClose())
                .wednesdayOpen(hours.getWednesdayOpen())
                .wednesdayClose(hours.getWednesdayClose())
                .thursdayOpen(hours.getThursdayOpen())
                .thursdayClose(hours.getThursdayClose())
                .fridayOpen(hours.getFridayOpen())
                .fridayClose(hours.getFridayClose())
                .saturdayOpen(hours.getSaturdayOpen())
                .saturdayClose(hours.getSaturdayClose())
                .sundayOpen(hours.getSundayOpen())
                .sundayClose(hours.getSundayClose())
                .closedMonday(hours.getClosedMonday())
                .closedTuesday(hours.getClosedTuesday())
                .closedWednesday(hours.getClosedWednesday())
                .closedThursday(hours.getClosedThursday())
                .closedFriday(hours.getClosedFriday())
                .closedSaturday(hours.getClosedSaturday())
                .closedSunday(hours.getClosedSunday())
                .build();
    }
    
    private RestaurantResponseDTO mapToResponseDTO(Restaurant restaurant) {
        return RestaurantResponseDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .address(restaurant.getAddress())
                .city(restaurant.getCity())
                .region(restaurant.getRegion())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .email(restaurant.getEmail())
                .phone(restaurant.getPhone())
                .website(restaurant.getWebsite())
                .type(restaurant.getType())
                .cuisineTypes(restaurant.getCuisineTypes())
                .specialties(restaurant.getSpecialties())
                .priceRange(restaurant.getPriceRange())
                .averagePricePerPerson(restaurant.getAveragePricePerPerson())
                .currency(restaurant.getCurrency())
                .seatingCapacity(restaurant.getSeatingCapacity())
                .acceptsReservations(restaurant.getAcceptsReservations())
                .openingHours(mapToOpeningHoursDTO(restaurant.getOpeningHours()))
                .services(restaurant.getServices())
                .paymentMethods(restaurant.getPaymentMethods())
                .spokenLanguages(restaurant.getSpokenLanguages())
                .photoUrls(restaurant.getPhotoUrls())
                .menuUrl(restaurant.getMenuUrl())
                .menuDescription(restaurant.getMenuDescription())
                .hasVegetarianOptions(restaurant.getHasVegetarianOptions())
                .hasVeganOptions(restaurant.getHasVeganOptions())
                .hasHalalOptions(restaurant.getHasHalalOptions())
                .hasGlutenFreeOptions(restaurant.getHasGlutenFreeOptions())
                .culturalSignificance(restaurant.getCulturalSignificance())
                .chefStory(restaurant.getChefStory())
                .localIngredients(restaurant.getLocalIngredients())
                .ambiance(restaurant.getAmbiance())
                .hasLiveMusic(restaurant.getHasLiveMusic())
                .hasOutdoorSeating(restaurant.getHasOutdoorSeating())
                .hasPrivateDining(restaurant.getHasPrivateDining())
                .partnershipStatus(restaurant.getPartnershipStatus())
                .specialOffersJOJ(restaurant.getSpecialOffersJOJ())
                .cateringAvailable(restaurant.getCateringAvailable())
                .maxCateringCapacity(restaurant.getMaxCateringCapacity())
                .verificationStatus(restaurant.getVerificationStatus())
                .hasHygieneRating(restaurant.getHasHygieneRating())
                .isHalalCertified(restaurant.getIsHalalCertified())
                .averageRating(restaurant.getAverageRating())
                .totalReviews(restaurant.getTotalReviews())
                .foodQualityRating(restaurant.getFoodQualityRating())
                .serviceRating(restaurant.getServiceRating())
                .ambianceRating(restaurant.getAmbianceRating())
                .valueForMoneyRating(restaurant.getValueForMoneyRating())
                .viewCount(restaurant.getViewCount())
                .reservationCount(restaurant.getReservationCount())
                .favoriteCount(restaurant.getFavoriteCount())
                .featured(restaurant.getFeatured())
                .active(restaurant.getActive())
                .createdAt(restaurant.getCreatedAt())
                .updatedAt(restaurant.getUpdatedAt())
                .build();
    }
}
