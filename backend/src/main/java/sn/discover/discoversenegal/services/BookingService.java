package sn.discover.discoversenegal.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.discover.discoversenegal.dto.*;
import sn.discover.discoversenegal.entities.*;
import sn.discover.discoversenegal.repositories.*;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public BookingResponseDTO createBooking(BookingCreateDTO dto) {
        log.info("Creating new booking for hotel ID: {}", dto.getHotelId());
        
        // Vérifier que l'hôtel existe
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hôtel non trouvé"));
        
        // Vérifier que l'utilisateur existe
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier les dates
        if (dto.getCheckOutDate().isBefore(dto.getCheckInDate()) || 
            dto.getCheckOutDate().isEqual(dto.getCheckInDate())) {
            throw new RuntimeException("Date de départ doit être après la date d'arrivée");
        }
        
        // Vérifier disponibilité
        if (!checkAvailability(dto.getHotelId(), dto.getCheckInDate(), dto.getCheckOutDate(), dto.getNumberOfRooms())) {
            throw new RuntimeException("Hôtel non disponible pour ces dates");
        }
        
        // Calculer nombre de nuits
        int numberOfNights = (int) ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        
        // Calculer prix total
        BigDecimal totalPrice = dto.getPricePerNight()
                .multiply(BigDecimal.valueOf(numberOfNights))
                .multiply(BigDecimal.valueOf(dto.getNumberOfRooms()));
        
        // Calculer taxes (exemple: 10%)
        BigDecimal taxes = totalPrice.multiply(BigDecimal.valueOf(0.10));
        
        // Frais de service (exemple: 5%)
        BigDecimal serviceFees = totalPrice.multiply(BigDecimal.valueOf(0.05));
        
        // Créer la réservation
        Booking booking = Booking.builder()
                .hotel(hotel)
                .user(user)
                .guestFirstName(dto.getGuestFirstName())
                .guestLastName(dto.getGuestLastName())
                .guestEmail(dto.getGuestEmail())
                .guestPhone(dto.getGuestPhone())
                .guestCountry(dto.getGuestCountry())
                .guestNationality(dto.getGuestNationality())
                .checkInDate(dto.getCheckInDate())
                .checkOutDate(dto.getCheckOutDate())
                .numberOfNights(numberOfNights)
                .numberOfAdults(dto.getNumberOfAdults())
                .numberOfChildren(dto.getNumberOfChildren() != null ? dto.getNumberOfChildren() : 0)
                .numberOfRooms(dto.getNumberOfRooms())
                .roomType(dto.getRoomType())
                .pricePerNight(dto.getPricePerNight())
                .totalPrice(totalPrice)
                .taxes(taxes)
                .serviceFees(serviceFees)
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "XOF")
                .discountCode(dto.getDiscountCode())
                .specialRequests(dto.getSpecialRequests())
                .needsAirportTransfer(dto.getNeedsAirportTransfer() != null ? dto.getNeedsAirportTransfer() : false)
                .flightNumber(dto.getFlightNumber())
                .arrivalTime(dto.getArrivalTime())
                .isOlympicBooking(dto.getIsOlympicBooking() != null ? dto.getIsOlympicBooking() : false)
                .olympicDelegation(dto.getOlympicDelegation())
                .olympicRole(dto.getOlympicRole())
                .languagePreference(dto.getLanguagePreference())
                .source(dto.getSource() != null ? dto.getSource() : BookingSource.WEB)
                .userIpAddress(dto.getUserIpAddress())
                .userAgent(dto.getUserAgent())
                .status(BookingStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created successfully with reference: {}", savedBooking.getBookingReference());
        
        return mapToResponseDTO(savedBooking);
    }
    
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        return mapToResponseDTO(booking);
    }
    
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingByReference(String reference) {
        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        return mapToResponseDTO(booking);
    }
    
    @Transactional
    public BookingResponseDTO updateBooking(Long id, BookingUpdateDTO dto) {
        log.info("Updating booking ID: {}", id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        // Vérifier que la réservation peut être modifiée
        if (booking.getStatus() == BookingStatus.CANCELLED || 
            booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Cette réservation ne peut plus être modifiée");
        }
        
        // Mise à jour des champs
        if (dto.getCheckInDate() != null) booking.setCheckInDate(dto.getCheckInDate());
        if (dto.getCheckOutDate() != null) booking.setCheckOutDate(dto.getCheckOutDate());
        if (dto.getNumberOfAdults() != null) booking.setNumberOfAdults(dto.getNumberOfAdults());
        if (dto.getNumberOfChildren() != null) booking.setNumberOfChildren(dto.getNumberOfChildren());
        if (dto.getNumberOfRooms() != null) booking.setNumberOfRooms(dto.getNumberOfRooms());
        if (dto.getRoomType() != null) booking.setRoomType(dto.getRoomType());
        if (dto.getSpecialRequests() != null) booking.setSpecialRequests(dto.getSpecialRequests());
        if (dto.getNeedsAirportTransfer() != null) booking.setNeedsAirportTransfer(dto.getNeedsAirportTransfer());
        if (dto.getFlightNumber() != null) booking.setFlightNumber(dto.getFlightNumber());
        if (dto.getArrivalTime() != null) booking.setArrivalTime(dto.getArrivalTime());
        
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Booking updated successfully");
        
        return mapToResponseDTO(updatedBooking);
    }
    
    @Transactional
    public BookingResponseDTO updateStatus(Long id, BookingStatusUpdateDTO dto) {
        log.info("Updating booking status ID: {} to {}", id, dto.getStatus());
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        booking.setStatus(dto.getStatus());
        
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Booking status updated successfully");
        
        return mapToResponseDTO(updatedBooking);
    }
    
    @Transactional
    public BookingResponseDTO updatePayment(Long id, PaymentUpdateDTO dto) {
        log.info("Updating payment for booking ID: {}", id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        booking.setPaymentStatus(dto.getPaymentStatus());
        booking.setPaymentMethod(dto.getPaymentMethod());
        booking.setTransactionId(dto.getTransactionId());
        
        if (dto.getPaymentStatus() == PaymentStatus.PAID) {
            booking.setPaymentDate(LocalDateTime.now());
            // Auto-confirmer la réservation si paiement complet
            if (booking.getStatus() == BookingStatus.PENDING) {
                booking.setStatus(BookingStatus.CONFIRMED);
            }
        }
        
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Payment updated successfully");
        
        return mapToResponseDTO(updatedBooking);
    }
    
    @Transactional
    public BookingResponseDTO confirmByHotel(Long id, HotelConfirmationDTO dto) {
        log.info("Hotel confirming booking ID: {}", id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        booking.setIsConfirmedByHotel(dto.getConfirmed());
        booking.setHotelConfirmationNotes(dto.getNotes());
        
        if (dto.getConfirmed()) {
            booking.setConfirmedByHotelAt(LocalDateTime.now());
            booking.setStatus(BookingStatus.CONFIRMED);
        }
        
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Hotel confirmation updated");
        
        return mapToResponseDTO(updatedBooking);
    }
    
    @Transactional
    public BookingResponseDTO checkIn(Long id, CheckInDTO dto) {
        log.info("Checking in booking ID: {}", id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("La réservation doit être confirmée pour faire le check-in");
        }
        
        booking.setActualCheckInTime(dto.getCheckInTime() != null ? dto.getCheckInTime() : LocalDateTime.now());
        booking.setStatus(BookingStatus.CHECKED_IN);
        
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Check-in completed successfully");
        
        return mapToResponseDTO(updatedBooking);
    }
    
    @Transactional
    public BookingResponseDTO checkOut(Long id, CheckOutDTO dto) {
        log.info("Checking out booking ID: {}", id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new RuntimeException("Le client doit être enregistré (check-in) pour faire le check-out");
        }
        
        booking.setActualCheckOutTime(dto.getCheckOutTime() != null ? dto.getCheckOutTime() : LocalDateTime.now());
        booking.setStatus(BookingStatus.CHECKED_OUT);
        
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Check-out completed successfully");
        
        return mapToResponseDTO(updatedBooking);
    }
    
    @Transactional
    public BookingResponseDTO cancelBooking(Long id, CancellationDTO dto) {
        log.info("Cancelling booking ID: {}", id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Cette réservation est déjà annulée");
        }
        
        if (booking.getStatus() == BookingStatus.CHECKED_OUT || 
            booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Cette réservation ne peut plus être annulée");
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(dto.getCancellationReason());
        
        // Calculer remboursement selon politique
        if (dto.getRequestRefund() != null && dto.getRequestRefund()) {
            BigDecimal refundAmount = calculateRefund(booking);
            booking.setRefundAmount(refundAmount);
            if (refundAmount.compareTo(BigDecimal.ZERO) > 0) {
                booking.setPaymentStatus(PaymentStatus.REFUNDED);
            }
        }
        
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Booking cancelled successfully");
        
        return mapToResponseDTO(updatedBooking);
    }
    
    @Transactional
    public BookingResponseDTO addReview(Long id, BookingReviewDTO dto) {
        log.info("Adding review for booking ID: {}", id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        if (booking.getStatus() != BookingStatus.CHECKED_OUT && 
            booking.getStatus() != BookingStatus.COMPLETED) {
            throw new RuntimeException("Vous pouvez seulement évaluer après votre séjour");
        }
        
        booking.setRating(dto.getRating());
        booking.setReview(dto.getReview());
        booking.setReviewedAt(LocalDateTime.now());
        booking.setStatus(BookingStatus.COMPLETED);
        
        // Mettre à jour la note moyenne de l'hôtel
        updateHotelRating(booking.getHotel().getId());
        
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Review added successfully");
        
        return mapToResponseDTO(updatedBooking);
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getHotelBookings(Long hotelId) {
        return bookingRepository.findByHotelIdOrderByCheckInDateDesc(hotelId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getPendingBookings() {
        return bookingRepository.findPendingBookings().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getTodayCheckIns(Long hotelId) {
        return bookingRepository.findTodayCheckIns(hotelId, LocalDate.now()).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getTodayCheckOuts(Long hotelId) {
        return bookingRepository.findTodayCheckOuts(hotelId, LocalDate.now()).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getCurrentGuests(Long hotelId) {
        return bookingRepository.findCurrentGuests(hotelId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getOlympicBookings() {
        return bookingRepository.findOlympicBookings().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public boolean checkAvailability(Long hotelId, LocalDate checkIn, LocalDate checkOut, Integer requestedRooms) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hôtel non trouvé"));
        
        Long overlappingBookings = bookingRepository.countOverlappingBookings(hotelId, checkIn, checkOut);
        
        // Vérifier si assez de chambres disponibles
        return (hotel.getTotalRooms() - overlappingBookings) >= requestedRooms;
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> searchBookings(BookingSearchDTO searchDTO) {
        return bookingRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (searchDTO.getHotelId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("hotel").get("id"), searchDTO.getHotelId()));
            }
            if (searchDTO.getUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), searchDTO.getUserId()));
            }
            if (searchDTO.getBookingReference() != null) {
                predicates.add(criteriaBuilder.equal(root.get("bookingReference"), searchDTO.getBookingReference()));
            }
            if (searchDTO.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), searchDTO.getStatus()));
            }
            if (searchDTO.getPaymentStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("paymentStatus"), searchDTO.getPaymentStatus()));
            }
            if (searchDTO.getIsOlympicBooking() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isOlympicBooking"), searchDTO.getIsOlympicBooking()));
            }
            if (searchDTO.getGuestEmail() != null) {
                predicates.add(criteriaBuilder.equal(root.get("guestEmail"), searchDTO.getGuestEmail()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    private BigDecimal calculateRefund(Booking booking) {
        // Logique simple de remboursement
        long daysUntilCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), booking.getCheckInDate());
        
        if (daysUntilCheckIn >= 14) {
            return booking.getFinalAmount(); // Remboursement complet
        } else if (daysUntilCheckIn >= 7) {
            return booking.getFinalAmount().multiply(BigDecimal.valueOf(0.50)); // 50%
        } else if (daysUntilCheckIn >= 3) {
            return booking.getFinalAmount().multiply(BigDecimal.valueOf(0.25)); // 25%
        } else {
            return BigDecimal.ZERO; // Pas de remboursement
        }
    }
    
    private void updateHotelRating(Long hotelId) {
        Double averageRating = bookingRepository.getAverageRating(hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        
        if (hotel != null && averageRating != null) {
            hotel.setAverageRating(averageRating);
            hotel.setTotalReviews(hotel.getTotalReviews() != null ? hotel.getTotalReviews() + 1 : 1);
            hotelRepository.save(hotel);
        }
    }
    
    private BookingResponseDTO mapToResponseDTO(Booking booking) {
        return BookingResponseDTO.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .hotelId(booking.getHotel().getId())
                .hotelName(booking.getHotel().getName())
                .hotelCity(booking.getHotel().getCity())
                .hotelAddress(booking.getHotel().getAddress())
                .guestFirstName(booking.getGuestFirstName())
                .guestLastName(booking.getGuestLastName())
                .guestEmail(booking.getGuestEmail())
                .guestPhone(booking.getGuestPhone())
                .guestCountry(booking.getGuestCountry())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .numberOfNights(booking.getNumberOfNights())
                .numberOfAdults(booking.getNumberOfAdults())
                .numberOfChildren(booking.getNumberOfChildren())
                .numberOfRooms(booking.getNumberOfRooms())
                .roomType(booking.getRoomType())
                .pricePerNight(booking.getPricePerNight())
                .totalPrice(booking.getTotalPrice())
                .taxes(booking.getTaxes())
                .serviceFees(booking.getServiceFees())
                .finalAmount(booking.getFinalAmount())
                .currency(booking.getCurrency())
                .discountAmount(booking.getDiscountAmount())
                .discountCode(booking.getDiscountCode())
                .status(booking.getStatus())
                .paymentStatus(booking.getPaymentStatus())
                .paymentMethod(booking.getPaymentMethod())
                .transactionId(booking.getTransactionId())
                .specialRequests(booking.getSpecialRequests())
                .needsAirportTransfer(booking.getNeedsAirportTransfer())
                .flightNumber(booking.getFlightNumber())
                .isOlympicBooking(booking.getIsOlympicBooking())
                .olympicDelegation(booking.getOlympicDelegation())
                .olympicRole(booking.getOlympicRole())
                .isConfirmedByHotel(booking.getIsConfirmedByHotel())
                .confirmedByHotelAt(booking.getConfirmedByHotelAt())
                .actualCheckInTime(booking.getActualCheckInTime())
                .actualCheckOutTime(booking.getActualCheckOutTime())
                .rating(booking.getRating())
                .review(booking.getReview())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}