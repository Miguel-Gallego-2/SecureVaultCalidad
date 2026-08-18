package co.edu.udea.securevault.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void contrasenaMenorA8CaracteresEsDebil() {
        // ARRANGE
        String password = "123456";

        // ACT
        Resultado resultado = validator.validate(password);

        // ASSERT
        assertFalse(resultado.esFuerte());
        assertEquals("DEBIL", resultado.nivel());
    }

    @Test
    void contrasenaFuerteEsReconocidaComoFuerte() {
        // ARRANGE
        String password = "MyP@ss123";

        // ACT
        Resultado resultado = validator.validate(password);

        // ASSERT
        assertTrue(resultado.esFuerte());
        assertEquals("FUERTE", resultado.nivel());
        assertEquals(9, resultado.puntuacion());
    }

    @Test
    void contrasenaSoloNumerosTienePuntuacionBaja() {
        // ARRANGE
        String password = "123456";

        // ACT
        int puntuacion = validator.calculateScore(password);

        // ASSERT
        assertEquals(2, puntuacion);
    }

    @Test
    void contrasenaSinMayusculaNoEsFuerte() {
        // ARRANGE
        String password = "abcdefg1";

        // ACT
        Resultado resultado = validator.validate(password);

        // ASSERT
        assertFalse(resultado.esFuerte());
    }

    @Test
    void contrasenaDe8CaracteresConMayusculasYNumerosEsFuerte() {
        // ARRANGE
        String password = "Abcdefg1";

        // ACT
        Resultado resultado = validator.validate(password);

        // ASSERT
        assertTrue(resultado.esFuerte());
        assertEquals("FUERTE", resultado.nivel());
    }

    @Test
    void contrasenaDeSoloMinusculasLargasEsMedia() {
        // ARRANGE
        String password = "abcdefgh";

        // ACT
        Resultado resultado = validator.validate(password);

        // ASSERT
        assertEquals("MEDIA", resultado.nivel());
        assertFalse(resultado.esFuerte());
    }

    @Test
    void contrasenaVaciaEsDebil() {
        // ARRANGE
        String password = "";

        // ACT
        Resultado resultado = validator.validate(password);

        // ASSERT
        assertFalse(resultado.esFuerte());
        assertEquals(0, resultado.puntuacion());
    }

    @Test
    void contrasenaNulaEsDebil() {
        // ARRANGE
        String password = null;

        // ACT
        Resultado resultado = validator.validate(password);

        // ASSERT
        assertFalse(resultado.esFuerte());
        assertEquals("DEBIL", resultado.nivel());
    }
}
