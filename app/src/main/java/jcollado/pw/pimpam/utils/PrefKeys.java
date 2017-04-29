package jcollado.pw.pimpam.utils;

public enum PrefKeys {
    NAME("MisPreferencias"),
    ID("id"),
    EMAIL("email"),
    PICURL("picurl"),
    LOGGED("logged"),
    COMICS("comics"),
    SERIES("series");

    private String value;

    PrefKeys(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
