package sn.discover.discoversenegal.entities;


public enum BookingStatus {
    PENDING,              // En attente de confirmation
    CONFIRMED,            // Confirmée par l'hôtel
    CHECKED_IN,           // Client arrivé
    CHECKED_OUT,          // Client parti
    CANCELLED,            // Annulée
    NO_SHOW,              // Client non présenté
    COMPLETED             // Séjour terminé
}

