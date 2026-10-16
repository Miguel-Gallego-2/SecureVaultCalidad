package co.edu.udea.securevault.acceptance;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Ejecuta todos los archivos .feature de src/test/resources/acceptance.
 *
 * Con @Tag("acceptance") esta clase NO corre en "gradlew test" (unitarias),
 * solo en "gradlew acceptanceTest" (y por tanto en "gradlew build").
 * El reporte HTML de Karate queda en build/karate-reports/karate-summary.html
 */
@Tag("acceptance")
class AcceptanceTest {

    @Test
    void ejecutarPruebasDeAceptacion() {
        Results resultados = Runner.path("classpath:acceptance")
                .reportDir("build/karate-reports")
                .parallel(1);

        assertEquals(0, resultados.getFailCount(), resultados.getErrorMessages());
    }
}
