package com.github.szgabsz91.foreignlanguagetester.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Language {

    @JsonProperty("hu")
    HU("hu"),

    @JsonProperty("en")
    EN("en"),

    @JsonProperty("de")
    DE("de");

    private final String languageCode;

    Language(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return this.languageCode;
    }

}
