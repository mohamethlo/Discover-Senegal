package sn.discover.discoversenegal.entities;

public enum PaymentStatus {
    PENDING,              // En attente de paiement
    PROCESSING,           // Paiement en cours de traitement
    PAID,                 // Payé
    PARTIALLY_PAID,       // Partiellement payé (acompte)
    FAILED,               // Échec du paiement
    REFUNDED,             // Remboursé
    PARTIALLY_REFUNDED    // Partiellement remboursé
}
