package co.edu.udea.securevault.core;

public final class PasswordValidator {

    public static final int PUNTUACION_MAXIMA = 10;
    public static final String NIVEL_DEBIL = "DEBIL";
    public static final String NIVEL_MEDIO = "MEDIA";
    public static final String NIVEL_FUERTE = "FUERTE";

    public Resultado validate(String password) {
        if (password == null || password.isEmpty()) {
            return new Resultado(0, PUNTUACION_MAXIMA, NIVEL_DEBIL, false);
        }
        int puntuacion = calculateScore(password);
        return new Resultado(puntuacion, PUNTUACION_MAXIMA, nivel(puntuacion), puntuacion >= 7);
    }

    public int calculateScore(String password) {
        int puntuacion = 0;

        if (password.length() >= 8) {
            puntuacion += 2;
        }
        if (password.length() >= 12) {
            puntuacion += 1;
        }
        if (contieneMayuscula(password)) {
            puntuacion += 2;
        }
        if (contieneMinuscula(password)) {
            puntuacion += 2;
        }
        if (contieneNumero(password)) {
            puntuacion += 2;
        }
        if (contieneEspecial(password)) {
            puntuacion += 1;
        }

        return puntuacion;
    }

    public static boolean contieneMayuscula(String password) {
        return password.chars().anyMatch(Character::isUpperCase);
    }

    public static boolean contieneMinuscula(String password) {
        return password.chars().anyMatch(Character::isLowerCase);
    }

    public static boolean contieneNumero(String password) {
        return password.chars().anyMatch(Character::isDigit);
    }

    public static boolean contieneEspecial(String password) {
        return password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));
    }

    private String nivel(int puntuacion) {
        if (puntuacion < 4) {
            return NIVEL_DEBIL;
        }
        if (puntuacion < 7) {
            return NIVEL_MEDIO;
        }
        return NIVEL_FUERTE;
    }
}
