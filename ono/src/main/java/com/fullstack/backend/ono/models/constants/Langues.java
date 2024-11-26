package com.fullstack.backend.ono.models.constants;

import java.util.Arrays;

public enum Langues {
    FRANCAIS("Français"),
    ANGLAIS("Anglais"),
    COREEN("Coréen"),
    UNKNOW("Unknow");

    private final String name;

    Langues(String name) {
        this.name = name;
    }

    public static Langues getByName(String name) {
        return Arrays.stream(Langues.values())
                .filter(langue -> langue.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(UNKNOW);
    }
    
}
