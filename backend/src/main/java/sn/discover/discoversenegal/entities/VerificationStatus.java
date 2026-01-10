package sn.discover.discoversenegal.entities;

public enum VerificationStatus {
    PENDING,           // En attente de vérification
    IN_REVIEW,         // En cours de vérification
    VERIFIED,          // Vérifié
    REJECTED,          // Rejeté
    REQUIRES_UPDATE    // Nécessite une mise à jour
}