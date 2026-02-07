package sn.discover.discoversenegal.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.discover.discoversenegal.dto.*;
import sn.discover.discoversenegal.services.BookingService;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BookingController {
    
    private final BookingService bookingService;
    
    /**
     * Créer une nouvelle réservation
     */
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingCreateDTO bookingDTO) {
        log.info("POST /api/bookings - Creating new booking for hotel: {}", bookingDTO.getHotelId());
        BookingResponseDTO response = bookingService.createBooking(bookingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Récupérer une réservation par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        log.info("GET /api/bookings/{} - Fetching booking details", id);
        BookingResponseDTO booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }
    
    /**
     * Récupérer une réservation par référence
     */
    @GetMapping("/reference/{reference}")
    public ResponseEntity<BookingResponseDTO> getBookingByReference(@PathVariable String reference) {
        log.info("GET /api/bookings/reference/{} - Fetching booking by reference", reference);
        BookingResponseDTO booking = bookingService.getBookingByReference(reference);
        return ResponseEntity.ok(booking);
    }
    
    /**
     * Mettre à jour une réservation
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingUpdateDTO bookingDTO) {
        log.info("PUT /api/bookings/{} - Updating booking", id);
        BookingResponseDTO response = bookingService.updateBooking(id, bookingDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Mettre à jour le statut d'une réservation
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody BookingStatusUpdateDTO statusDTO) {
        log.info("PATCH /api/bookings/{}/status - Updating status to {}", id, statusDTO.getStatus());
        BookingResponseDTO response = bookingService.updateStatus(id, statusDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Mettre à jour le paiement
     */
    @PatchMapping("/{id}/payment")
    public ResponseEntity<BookingResponseDTO> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentUpdateDTO paymentDTO) {
        log.info("PATCH /api/bookings/{}/payment - Updating payment", id);
        BookingResponseDTO response = bookingService.updatePayment(id, paymentDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Confirmation par l'hôtel
     */
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<BookingResponseDTO> confirmByHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelConfirmationDTO confirmationDTO) {
        log.info("PATCH /api/bookings/{}/confirm - Hotel confirming booking", id);
        BookingResponseDTO response = bookingService.confirmByHotel(id, confirmationDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Check-in
     */
    @PatchMapping("/{id}/checkin")
    public ResponseEntity<BookingResponseDTO> checkIn(
            @PathVariable Long id,
            @Valid @RequestBody CheckInDTO checkInDTO) {
        log.info("PATCH /api/bookings/{}/checkin - Checking in guest", id);
        BookingResponseDTO response = bookingService.checkIn(id, checkInDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Check-out
     */
    @PatchMapping("/{id}/checkout")
    public ResponseEntity<BookingResponseDTO> checkOut(
            @PathVariable Long id,
            @Valid @RequestBody CheckOutDTO checkOutDTO) {
        log.info("PATCH /api/bookings/{}/checkout - Checking out guest", id);
        BookingResponseDTO response = bookingService.checkOut(id, checkOutDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Annuler une réservation
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @PathVariable Long id,
            @Valid @RequestBody CancellationDTO cancellationDTO) {
        log.info("PATCH /api/bookings/{}/cancel - Cancelling booking", id);
        BookingResponseDTO response = bookingService.cancelBooking(id, cancellationDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Ajouter un avis
     */
    @PatchMapping("/{id}/review")
    public ResponseEntity<BookingResponseDTO> addReview(
            @PathVariable Long id,
            @Valid @RequestBody BookingReviewDTO reviewDTO) {
        log.info("PATCH /api/bookings/{}/review - Adding review", id);
        BookingResponseDTO response = bookingService.addReview(id, reviewDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Récupérer toutes les réservations d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getUserBookings(@PathVariable Long userId) {
        log.info("GET /api/bookings/user/{} - Fetching user bookings", userId);
        List<BookingResponseDTO> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Récupérer toutes les réservations d'un hôtel
     */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<BookingResponseDTO>> getHotelBookings(@PathVariable Long hotelId) {
        log.info("GET /api/bookings/hotel/{} - Fetching hotel bookings", hotelId);
        List<BookingResponseDTO> bookings = bookingService.getHotelBookings(hotelId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Récupérer les réservations en attente
     */
    @GetMapping("/pending")
    public ResponseEntity<List<BookingResponseDTO>> getPendingBookings() {
        log.info("GET /api/bookings/pending - Fetching pending bookings");
        List<BookingResponseDTO> bookings = bookingService.getPendingBookings();
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Arrivées du jour pour un hôtel
     */
    @GetMapping("/hotel/{hotelId}/arrivals/today")
    public ResponseEntity<List<BookingResponseDTO>> getTodayCheckIns(@PathVariable Long hotelId) {
        log.info("GET /api/bookings/hotel/{}/arrivals/today - Fetching today's arrivals", hotelId);
        List<BookingResponseDTO> bookings = bookingService.getTodayCheckIns(hotelId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Départs du jour pour un hôtel
     */
    @GetMapping("/hotel/{hotelId}/departures/today")
    public ResponseEntity<List<BookingResponseDTO>> getTodayCheckOuts(@PathVariable Long hotelId) {
        log.info("GET /api/bookings/hotel/{}/departures/today - Fetching today's departures", hotelId);
        List<BookingResponseDTO> bookings = bookingService.getTodayCheckOuts(hotelId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Clients actuellement à l'hôtel
     */
    @GetMapping("/hotel/{hotelId}/current-guests")
    public ResponseEntity<List<BookingResponseDTO>> getCurrentGuests(@PathVariable Long hotelId) {
        log.info("GET /api/bookings/hotel/{}/current-guests - Fetching current guests", hotelId);
        List<BookingResponseDTO> bookings = bookingService.getCurrentGuests(hotelId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Réservations JOJ 2026
     */
    @GetMapping("/olympic")
    public ResponseEntity<List<BookingResponseDTO>> getOlympicBookings() {
        log.info("GET /api/bookings/olympic - Fetching Olympic bookings");
        List<BookingResponseDTO> bookings = bookingService.getOlympicBookings();
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Vérifier disponibilité
     */
    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long hotelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam Integer rooms) {
        log.info("GET /api/bookings/availability - Checking availability for hotel {}", hotelId);
        boolean available = bookingService.checkAvailability(hotelId, checkIn, checkOut, rooms);
        return ResponseEntity.ok(available);
    }
    
    /**
     * Recherche avancée
     */
    @PostMapping("/search")
    public ResponseEntity<List<BookingResponseDTO>> searchBookings(
            @RequestBody BookingSearchDTO searchDTO) {
        log.info("POST /api/bookings/search - Performing advanced search");
        List<BookingResponseDTO> bookings = bookingService.searchBookings(searchDTO);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Booking Service is running");
    }
}