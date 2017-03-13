package jcollado.pw.pimpam.utils;

public enum PrefKeys {
    NAME("MisPreferencias"),
    ID("id"),
    IDEMPRESA("idEmpresa");

    private String value;

    PrefKeys(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
