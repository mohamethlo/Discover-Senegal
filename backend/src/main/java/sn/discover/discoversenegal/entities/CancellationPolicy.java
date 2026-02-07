package sn.discover.discoversenegal.entities;

public enum CancellationPolicy {
    FLEXIBLE,             // Annulation gratuite jusqu'à 24h avant
    MODERATE,             // Annulation gratuite jusqu'à 7 jours avant
    STRICT,               // Annulation gratuite jusqu'à 14 jours avant
    NON_REFUNDABLE,       // Non remboursable
    CUSTOM                // Politique personnalisée
}
