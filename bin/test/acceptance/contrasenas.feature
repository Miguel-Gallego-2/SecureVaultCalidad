Feature: Validacion y generacion de contrasenas (menu de consola)

  Background:
    * def ConsoleApp = Java.type('co.edu.udea.securevault.acceptance.ConsoleApp')

  Scenario Outline: Validar la contrasena "<contrasena>" da <nivel> con <puntos>/10
    * def salida = ConsoleApp.run('5\n<contrasena>\n0\n')
    # Se colapsan los espacios para no depender de la alineacion de la tabla impresa
    * def plano = salida.replace(/\s+/g, ' ')
    Then match plano contains 'Puntuacion: <puntos>/10'
    And match plano contains 'Nivel: <nivel>'

    Examples:
      | contrasena   | puntos | nivel  |
      | abc          | 2      | DEBIL  |
      | abcdefgh     | 4      | MEDIA  |
      | Abcdef12     | 8      | FUERTE |
      | Abcdefg1!xyz | 10     | FUERTE |

  Scenario: La validacion informa que caracteres contiene la contrasena
    * def salida = ConsoleApp.run('5\nAbcdef12\n0\n')
    * def plano = salida.replace(/\s+/g, ' ')
    Then match plano contains 'Longitud: 8'
    And match plano contains 'Mayusculas: Si'
    And match plano contains 'Minusculas: Si'
    And match plano contains 'Numeros: Si'
    And match plano contains 'Especiales: No'

  Scenario: Una contrasena generada tiene la longitud pedida y los cuatro tipos de caracter
    * def salida = ConsoleApp.run('6\n12\n0\n')
    * def generada = salida.split('Contrasena generada: ')[1].split('\n')[0].trim()
    * assert generada.length == 12
    * assert /[A-Z]/.test(generada)
    * assert /[a-z]/.test(generada)
    * assert /[0-9]/.test(generada)
    * assert /[^A-Za-z0-9]/.test(generada)

  Scenario: Una contrasena generada se considera fuerte al validarla
    * def salida1 = ConsoleApp.run('6\n16\n0\n')
    * def generada = salida1.split('Contrasena generada: ')[1].split('\n')[0].trim()
    * def salida2 = ConsoleApp.run('5\n' + generada + '\n0\n')
    * def plano = salida2.replace(/\s+/g, ' ')
    Then match plano contains 'Nivel: FUERTE'
