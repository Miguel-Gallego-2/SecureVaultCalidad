# SecureVaultCalidad
App de prueba

Gestor de credenciales de consola (Java 21, Gradle) con control de calidad automatizado.

## Comandos

| Comando | Qué hace |
|---|---|
| `./gradlew test` | Pruebas unitarias (JUnit) |
| `./gradlew acceptanceTest` | Pruebas de aceptación (Karate) |
| `./gradlew build jacocoTestReport` | Todo lo anterior + reporte de cobertura (JaCoCo) |
| `./gradlew jar` | JAR ejecutable en `build/libs/` |

## Calidad y CI/CD

Cada push a `main` ejecuta el pipeline de GitHub Actions: compila, corre las pruebas,
mide cobertura, analiza en SonarCloud (con Quality Gate) y, si todo pasa, publica un Release
con el JAR.

Detalles y pasos de configuración: [docs/GUIA_CALIDAD.md](docs/GUIA_CALIDAD.md)
