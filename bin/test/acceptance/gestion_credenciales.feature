Feature: Gestion de credenciales en SecureVault (menu de consola)

  # Cada "\n" equivale a pulsar Enter. Siempre termina en "0" (Salir).
  # Menu: 1 Agregar | 2 Buscar | 3 Actualizar | 4 Eliminar | 5 Validar | 6 Generar | 0 Salir

  Background:
    * def ConsoleApp = Java.type('co.edu.udea.securevault.acceptance.ConsoleApp')

  Scenario: El menu principal se muestra y la app termina con la opcion 0
    * def salida = ConsoleApp.run('0\n')
    Then match salida contains '===== SecureVault ====='
    And match salida contains '1. Agregar credencial'
    And match salida contains '0. Salir'
    And match salida contains 'Saliendo...'

  Scenario: Agregar una credencial y luego buscarla
    * def salida = ConsoleApp.run('1\nGitHub\nmiguel\nClave123!\n2\nGitHub\n0\n')
    Then match salida contains 'Credencial agregada.'
    And match salida contains 'Servicio: GitHub'
    And match salida contains 'Usuario: miguel'
    And match salida contains 'Contrasena: Clave123!'

  Scenario: No se puede agregar dos veces el mismo servicio
    * def salida = ConsoleApp.run('1\nGitHub\nmiguel\nClave123!\n1\nGitHub\notro\nOtra456?\n0\n')
    Then match salida contains 'Credencial agregada.'
    And match salida contains 'El servicio ya existe.'

  Scenario: Buscar un servicio que no existe
    * def salida = ConsoleApp.run('2\nNetflix\n0\n')
    Then match salida contains 'No se encontro la credencial.'

  Scenario: Actualizar la contrasena de una credencial existente
    * def salida = ConsoleApp.run('1\nGitHub\nmiguel\nClave123!\n3\nGitHub\nNuevaClave9#\n2\nGitHub\n0\n')
    Then match salida contains 'Contrasena actualizada.'
    And match salida contains 'Contrasena: NuevaClave9#'
    And match salida !contains 'Contrasena: Clave123!'

  Scenario: Actualizar una credencial que no existe
    * def salida = ConsoleApp.run('3\nNetflix\nloquesea\n0\n')
    Then match salida contains 'No se encontro la credencial.'
    And match salida !contains 'Contrasena actualizada.'

  Scenario: Eliminar una credencial y comprobar que ya no se encuentra
    * def salida = ConsoleApp.run('1\nGitHub\nmiguel\nClave123!\n4\nGitHub\n2\nGitHub\n0\n')
    Then match salida contains 'Credencial eliminada.'
    And match salida contains 'No se encontro la credencial.'

  Scenario: Eliminar una credencial que no existe
    * def salida = ConsoleApp.run('4\nNetflix\n0\n')
    Then match salida contains 'No se encontro la credencial.'
    And match salida !contains 'Credencial eliminada.'

  Scenario: Una opcion numerica fuera del menu se rechaza
    * def salida = ConsoleApp.run('9\n0\n')
    Then match salida contains 'Opcion no valida'
