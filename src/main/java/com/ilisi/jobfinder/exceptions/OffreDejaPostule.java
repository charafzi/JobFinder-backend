package com.ilisi.jobfinder.exceptions;

public class OffreDejaPostule extends RuntimeException {
    public OffreDejaPostule() {
        super("Vous avez déjà postulé à cette offre.");
    }
}
