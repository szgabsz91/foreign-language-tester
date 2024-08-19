package com.github.szgabsz91.foreignlanguagetester.models;

public enum OutputColor {

    WHITE("\u001B[37m"),
    YELLOW("\u001B[33m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m");

    private final String ansiCode;

    OutputColor(String ansiCode) {
        this.ansiCode = ansiCode;
    }

    public String getAnsiCode() {
        return this.ansiCode;
    }

}
