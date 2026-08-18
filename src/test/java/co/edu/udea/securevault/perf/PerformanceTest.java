package co.edu.udea.securevault.perf;

import co.edu.udea.securevault.core.PasswordGenerator;
import co.edu.udea.securevault.model.Credential;
import co.edu.udea.securevault.store.ListCredentialStore;
import co.edu.udea.securevault.store.MapCredentialStore;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PerformanceTest {

    private static final int CANTIDAD = 100_000;

    private static double medir(Runnable tarea, int warmup, int iteraciones) {
        for (int i = 0; i < warmup; i++) {
            tarea.run();
        }
        long inicio = System.nanoTime();
        long acc = 0;
        for (int i = 0; i < iteraciones; i++) {
            if (acc == Long.MIN_VALUE) System.out.println();
            tarea.run();
        }
        long transcurrido = System.nanoTime() - inicio;
        return transcurrido / (double) iteraciones;
    }

    private static ListCredentialStore cargarLista(int cantidad) {
        ListCredentialStore store = new ListCredentialStore();
        for (int i = 0; i < cantidad; i++) {
            store.add(new Credential("Servicio" + i, "usuario" + i, "Passw0rd!"));
        }
        return store;
    }

    private static MapCredentialStore cargarMapa(int cantidad) {
        MapCredentialStore store = new MapCredentialStore();
        for (int i = 0; i < cantidad; i++) {
            store.add(new Credential("Servicio" + i, "usuario" + i, "Passw0rd!"));
        }
        return store;
    }

    @Test
    void busquedaPorMapaEsMasRapidaQuePorLista() {
        // ARRANGE: 100.000 credenciales; se busca la ultima (peor caso para la lista)
        ListCredentialStore lista = cargarLista(CANTIDAD);
        MapCredentialStore mapa = cargarMapa(CANTIDAD);
        String servicio = "Servicio" + (CANTIDAD - 1);

        // ACT: medir nanosegundos por operacion con warm-up
        double tiempoLista = medir(() -> lista.find(servicio), 100, 1_000);
        double tiempoMapa = medir(() -> mapa.find(servicio), 100, 1_000);

        // ASSERT
        System.out.printf("[Perf] Busqueda: Lista=%.2f ns/op | Mapa=%.2f ns/op | Mapa es %.1fx mas rapido%n",
                tiempoLista, tiempoMapa, tiempoLista / tiempoMapa);
        assertTrue(tiempoMapa * 10 < tiempoLista, "El mapa deberia ser mucho mas rapido que la lista");
    }

    @Test
    void secureRandomEsMasLentoQueRandom() {
        // ARRANGE: dos generadores, uno rapido (Random) y otro seguro (SecureRandom)
        PasswordGenerator rapido = new PasswordGenerator(new Random(42));
        PasswordGenerator seguro = new PasswordGenerator(new SecureRandom());

        // ACT: medir nanosegundos por generacion con warm-up
        double tiempoRapido = medir(() -> rapido.generar(16), 2_000, 1_000_000);
        double tiempoSeguro = medir(() -> seguro.generar(16), 2_000, 1_000_000);

        // ASSERT
        System.out.printf("[Perf] Generacion: Random=%.2f ns/op | SecureRandom=%.2f ns/op | SecureRandom es %.1fx mas lento%n",
                tiempoRapido, tiempoSeguro, tiempoSeguro / tiempoRapido);
        assertTrue(tiempoSeguro > tiempoRapido, "SecureRandom deberia ser mas lento que Random");
    }

    @Test
    void busquedaEnListaEscalaDeFormaLineal() {
        // ARRANGE: listas de distinto tamano
        ListCredentialStore pequeña = cargarLista(10_000);
        ListCredentialStore mediana = cargarLista(100_000);
        ListCredentialStore grande = cargarLista(1_000_000);

        // ACT: buscar un servicio inexistente (recorre toda la lista)
        double tiempoPequeña = medir(() -> pequeña.find("NoExiste"), 100, 2_000);
        double tiempoMediana = medir(() -> mediana.find("NoExiste"), 100, 200);
        double tiempoGrande = medir(() -> grande.find("NoExiste"), 100, 20);

        // ASSERT
        System.out.printf("[Perf] Escalado lista: 10k=%.2f ns/op | 100k=%.2f ns/op | 1M=%.2f ns/op%n",
                tiempoPequeña, tiempoMediana, tiempoGrande);
        assertTrue(tiempoMediana > tiempoPequeña * 5, "Al multiplicar por 10 el tamano, el tiempo debe crecer ~10x");
        assertTrue(tiempoGrande > tiempoMediana * 5, "Al multiplicar por 10 el tamano, el tiempo debe crecer ~10x");
    }
}
