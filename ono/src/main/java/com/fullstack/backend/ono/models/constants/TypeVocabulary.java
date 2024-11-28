package com.fullstack.backend.ono.models.constants;

import java.util.Arrays;

public enum TypeVocabulary {
    NAMEBRE("Namebre"),
    NAME("Name"),
    ADJECTIF("Adjectif"),
    VERBE("Verbe"),
    PREPOSITION("Preposition"),
    UNKNOW("Unknow");

    private final String name;

    TypeVocabulary(String name) {
        this.name = name;
    }

    public static TypeVocabulary getByName(String name) {
        return Arrays.stream(TypeVocabulary.values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(UNKNOW);
    }
    
}
