package ProyUniversidad.UVI.utils;

import java.util.regex.Pattern;

public class Validaciones {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern DNI_PATTERN = Pattern.compile("^\\d{8}$");

    public static boolean esEmailValido(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean esDniValido(String dni) {
        return dni != null && DNI_PATTERN.matcher(dni).matches();
    }

    public static void validarTextoNoVacio(String texto, String nombreCampo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " no puede estar vacío");
        }
    }
}