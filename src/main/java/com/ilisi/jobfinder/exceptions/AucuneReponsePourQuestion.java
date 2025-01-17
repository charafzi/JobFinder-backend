package com.ilisi.jobfinder.exceptions;

public class AucuneReponsePourQuestion extends RuntimeException {
    public AucuneReponsePourQuestion() {
        super("Vous avez pas fournie une réponse pour la question de l'offre.");
    }
}
