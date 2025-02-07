package com.ilisi.jobfinder.Enum;

public enum SortBy {
    PUB_DATE("datePublication"),
    CANDIDATURE_DATE("dateCandidature"),
    SALARY("salaire");

    private final String field;

    SortBy(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
