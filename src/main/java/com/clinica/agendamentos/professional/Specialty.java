package com.clinica.agendamentos.professional;

public enum Specialty {
    GENERAL_SURGERY("Cirurgia Geral"),
    ORTHOPEDICS("Ortopedia"),
    OBSTETRICS_AND_GYNECOLOGY("Obstetrícia e Ginecologia"),
    ANESTHESIOLOGY("Anestesiologia");

    private final String displayName;

    Specialty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
