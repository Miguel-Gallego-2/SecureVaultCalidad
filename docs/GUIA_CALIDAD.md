# Guía de calidad: SonarCloud + JaCoCo + Karate + GitHub Actions

Esta guía explica qué es cada herramienta, qué quedó configurado en el proyecto y
qué pasos tienes que hacer tú **a mano** en SonarCloud y en GitHub (no se pueden
automatizar desde el código).

## 1. Qué es cada cosa

| Herramienta | Para qué sirve | Dónde está en el proyecto |
|---|---|---|
| **JaCoCo** | Mide la **cobertura**: qué porcentaje de líneas/ramas de tu código ejecutaron las pruebas. Genera un reporte XML (para Sonar) y HTML (para ti). | plugin `jacoco` en `build.gradle` |
| **SonarCloud** (hoy se llama también *SonarQube Cloud*) | Servicio web que **analiza el código** y muestra: *code smells*, bugs, vulnerabilidades, duplicación, complejidad, deuda técnica y cobertura. Decide si el código "pasa" con el **Quality Gate**. | plugin `org.sonarqube` + bloque `sonar { }` en `build.gradle` |
| **Karate** | Framework para **pruebas de aceptación**: se escriben en archivos `.feature` (casi lenguaje natural) y comprueban el programa como lo usaría una persona. | `src/test/resources/acceptance/*.feature` |
| **GitHub Actions** | Ejecuta todo lo anterior automáticamente en la nube en cada `push` (CI) y, si todo pasa, entrega el programa (CD). | `.github/workflows/ci-cd.yml` |

Conceptos que pide el trabajo:

- **Quality Gate**: conjunto de condiciones que el código debe cumplir (por ejemplo, cobertura ≥ 80 % en código nuevo). Si falla, el análisis queda en rojo y el pipeline **no entrega**.
- **Code smell**: código que funciona pero es difícil de mantener (método largo, variable sin usar, etc.).
- **Deuda técnica**: tiempo estimado que tomaría arreglar todos los *code smells*. Sonar la da en minutos/horas/días y como **ratio de deuda** (A = ≤ 5 %).
- **Complejidad ciclomática**: número de caminos independientes del código (cada `if`, `for`, `case`, `&&`… suma 1).
- **Complejidad cognitiva**: qué tan difícil es *entender* el código para una persona; penaliza más los bloques anidados.
- **Aceptación vs. unitarias**: las unitarias prueban una clase aislada; las de aceptación prueban el programa completo desde el menú.

## 2. Cómo está armado el proyecto

- **Pruebas unitarias (JUnit):** `./gradlew test`
- **Pruebas de aceptación (Karate):** `./gradlew acceptanceTest`
  - `ConsoleApp.java` ejecuta `Main` con un texto como "teclado" y devuelve lo que imprimió.
  - Los `.feature` llaman a `ConsoleApp.run('1\nGitHub\nmiguel\nClave123!\n0\n')` (cada `\n` es un Enter) y comprueban la salida.
  - Reporte: `build/karate-reports/karate-summary.html`
- **Todo junto + cobertura:** `./gradlew build jacocoTestReport`
  - Reporte HTML: `build/reports/jacoco/test/html/index.html`
  - La cobertura combina unitarias **y** aceptación (así `Main` también cuenta).
- **JAR ejecutable:** `./gradlew jar` y luego `java -jar build/libs/SecureVault-1.0.0.jar`

## 3. Pasos manuales (una sola vez)

### 3.1 Crear el proyecto en SonarCloud
1. Entra a <https://sonarcloud.io> e inicia sesión **con GitHub**.
2. Importa tu cuenta/organización de GitHub y elige el repositorio `SecureVaultCalidad`.
3. Cuando pregunte por el método de análisis elige **GitHub Actions** (análisis por CI).
4. Para "New Code" deja *Previous version* (por defecto).
5. Anota dos valores que Sonar te muestra: **Organization key** y **Project key**.
6. Abre `build.gradle`, busca el bloque `sonar { properties { ... } }` y **ajusta
   `sonar.organization` y `sonar.projectKey`** si son distintos de los que están puestos
   (los actuales son los que Sonar suele sugerir: `miguel-gallego-2` y
   `Miguel-Gallego-2_SecureVaultCalidad`).
7. En SonarCloud: **Administration → Analysis Method** y **desactiva "Automatic Analysis"**
   (si está activo, choca con el análisis por CI y da error).

### 3.2 Crear el token y guardarlo en GitHub
1. SonarCloud: tu avatar → **My Account → Security → Generate Tokens**. Copia el token (solo se ve una vez).
2. GitHub, en tu repositorio: **Settings → Secrets and variables → Actions → New repository secret**.
3. Nombre: `SONAR_TOKEN`. Valor: el token.

### 3.3 Subir los cambios
```bash
git add .
git commit -m "Agrega JaCoCo, SonarCloud, Karate y pipeline CI/CD"
git push
```
Ve a la pestaña **Actions** del repositorio y mira correr el workflow `CI-CD`.

## 4. Quality Gate

SonarCloud trae por defecto el gate **"Sonar way"**. Sus condiciones sobre **código nuevo** son:

| Condición | Umbral |
|---|---|
| Reliability rating (bugs) | A |
| Security rating (vulnerabilidades) | A |
| Maintainability rating (deuda técnica) | A |
| Security hotspots revisados | 100 % |
| Cobertura | ≥ 80 % |
| Duplicación | ≤ 3 % |

> **Ojo con el plan:** según la documentación de Sonar, **crear gates propios es de los
> planes Team/Enterprise**. En el plan gratuito normalmente solo puedes usar "Sonar way".
> Comprueba en tu cuenta (**Organization → Quality Gates**) qué te permite. Si no puedes
> crear uno propio, en tu informe documenta que usas "Sonar way" y explica cada condición
> (la tabla de arriba te sirve).

El pipeline ya está conectado al gate: en `build.gradle` está `sonar.qualitygate.wait=true`,
así que si el gate falla, `./gradlew sonar` falla, el job queda en rojo y **no se ejecuta la entrega**.

## 5. Dónde ver cada métrica que pide el trabajo

En SonarCloud, abre el proyecto (los nombres de menú pueden variar ligeramente):

- **Quality Gate:** pestaña *Overview* (Passed / Failed y qué condición falló).
- **Deuda técnica:** *Measures → Maintainability* → *Technical Debt* y *Debt Ratio*.
- **Code smells:** pestaña *Issues*, filtro *Type = Code Smell* (también el conteo en *Measures → Maintainability*).
- **Complejidad ciclomática y cognitiva:** *Measures → Complexity*; puedes ver el detalle por archivo.
- **Cobertura:** *Measures → Coverage* (por archivo, líneas cubiertas y no cubiertas).
- **Duplicación:** *Measures → Duplications*.

Para el informe, toma capturas de cada una de estas pantallas.

## 6. Problemas frecuentes

| Síntoma | Causa y solución |
|---|---|
| `You are running CI analysis while Automatic Analysis is enabled` | Desactiva *Automatic Analysis* (paso 3.1.7). |
| `Project not found` / `Could not find organization` | `sonar.projectKey` o `sonar.organization` no coinciden con SonarCloud (paso 3.1.6). |
| `Please provide a SonarCloud token` / `401` | Falta el secreto `SONAR_TOKEN` o está mal copiado (paso 3.2). |
| Cobertura 0 % en Sonar | Verifica que exista `build/reports/jacoco/test/jacocoTestReport.xml` y que la ruta en `build.gradle` sea esa. |
| Quality Gate falla por cobertura < 80 % | Agrega pruebas para el código no cubierto (el reporte HTML de JaCoCo te lo marca en rojo). |
| `OutputDirectoryProvider not available` al correr pruebas | Versiones de JUnit desalineadas. Ejecuta `./gradlew dependencies --configuration testRuntimeClasspath` y alinea las versiones `org.junit.*` (por ejemplo con `testImplementation platform('org.junit:junit-bom:<version>')`). |
| `PerformanceTest` falla de vez en cuando | Mide tiempos reales; en máquinas lentas o compartidas (como CI) puede fallar sin que haya un error en el código. |
| En Windows el comando `./gradlew` no funciona | Usa `gradlew.bat` (PowerShell: `.\gradlew.bat build`). |

## 7. Correr SonarCloud desde tu computador (opcional)

```powershell
# PowerShell (Windows)
$env:SONAR_TOKEN = "tu_token"
.\gradlew.bat build sonar
```
```bash
# Linux / macOS
SONAR_TOKEN=tu_token ./gradlew build sonar
```
Nunca escribas el token dentro de `build.gradle` ni lo subas a git.
