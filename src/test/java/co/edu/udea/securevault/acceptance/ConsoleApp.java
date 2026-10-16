package co.edu.udea.securevault.acceptance;

import co.edu.udea.securevault.cli.Main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Puente entre Karate y la aplicacion de consola.
 *
 * Karate no puede "teclear" en un programa de consola, asi que esta clase hace
 * ese trabajo: ejecuta Main con un texto como entrada estandar (lo que el
 * usuario escribiria, una linea por Enter) y devuelve todo lo que el programa
 * imprimio. Los archivos .feature llaman a {@link #run(String)}.
 *
 * Como Main se ejecuta dentro de la misma JVM de las pruebas, JaCoCo tambien
 * mide la cobertura del menu (Main).
 */
public final class ConsoleApp {

    private ConsoleApp() {
    }

    public static String run(String entrada) {
        InputStream entradaOriginal = System.in;
        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try (PrintStream salidaCapturada = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setIn(new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)));
            System.setOut(salidaCapturada);
            Main.main(new String[0]);
        } finally {
            System.setIn(entradaOriginal);
            System.setOut(salidaOriginal);
        }

        return buffer.toString(StandardCharsets.UTF_8);
    }
}
