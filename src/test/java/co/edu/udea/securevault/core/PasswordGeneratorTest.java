package co.edu.udea.securevault.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordGeneratorTest {

    private final PasswordGenerator generator = new PasswordGenerator();

    @Test
    void generaContrasenaConLaLongitudSolicitada() {
        // ARRANGE
        int longitud = 16;

        // ACT
        String contrasena = generator.generar(longitud);

        // ASSERT
        assertEquals(longitud, contrasena.length());
    }

    @Test
    void generaContrasenaConTodasLasClasesDeCaracteres() {
        // ARRANGE
        int longitud = 12;

        // ACT
        String contrasena = generator.generar(longitud);

        // ASSERT
        assertTrue(PasswordValidator.contieneMayuscula(contrasena));
        assertTrue(PasswordValidator.contieneMinuscula(contrasena));
        assertTrue(PasswordValidator.contieneNumero(contrasena));
        assertTrue(PasswordValidator.contieneEspecial(contrasena));
    }

    @Test
    void dosGeneracionesSonDistintas() {
        // ARRANGE
        int longitud = 16;

        // ACT
        String primera = generator.generar(longitud);
        String segunda = generator.generar(longitud);

        // ASSERT
        assertNotEquals(primera, segunda);
    }

    @Test
    void longitudMenorA4LanzaExcepcion() {
        // ARRANGE
        int longitud = 3;

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> generator.generar(longitud));
    }
}
