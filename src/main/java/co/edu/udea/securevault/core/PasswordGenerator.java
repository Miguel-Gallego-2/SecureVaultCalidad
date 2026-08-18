package co.edu.udea.securevault.core;

import java.security.SecureRandom;
import java.util.Random;

public final class PasswordGenerator {

    private static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMEROS = "0123456789";
    private static final String ESPECIALES = "!@#$%^&*()-_=+";
    private static final String TODOS = MAYUSCULAS + MINUSCULAS + NUMEROS + ESPECIALES;

    private final Random random;

    public PasswordGenerator() {
        this(new SecureRandom());
    }

    public PasswordGenerator(Random random) {
        this.random = random;
    }

    public String generar(int longitud) {
        if (longitud < 4) {
            throw new IllegalArgumentException("La longitud minima es 4");
        }

        StringBuilder sb = new StringBuilder(longitud);
        sb.append(caracterDe(MAYUSCULAS));
        sb.append(caracterDe(MINUSCULAS));
        sb.append(caracterDe(NUMEROS));
        sb.append(caracterDe(ESPECIALES));
        for (int i = 4; i < longitud; i++) {
            sb.append(caracterDe(TODOS));
        }
        return barajar(sb.toString());
    }

    private char caracterDe(String conjunto) {
        return conjunto.charAt(random.nextInt(conjunto.length()));
    }

    private String barajar(String valor) {
        char[] caracteres = valor.toCharArray();
        for (int i = caracteres.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char tmp = caracteres[i];
            caracteres[i] = caracteres[j];
            caracteres[j] = tmp;
        }
        return new String(caracteres);
    }
}
