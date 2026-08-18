package co.edu.udea.securevault.cli;

import co.edu.udea.securevault.core.PasswordGenerator;
import co.edu.udea.securevault.core.PasswordValidator;
import co.edu.udea.securevault.core.Resultado;
import co.edu.udea.securevault.model.Credential;
import co.edu.udea.securevault.service.PasswordManager;
import co.edu.udea.securevault.store.MapCredentialStore;

import java.util.Optional;
import java.util.Scanner;

public final class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PasswordManager manager = new PasswordManager(new MapCredentialStore());
        PasswordValidator validator = new PasswordValidator();
        PasswordGenerator generator = new PasswordGenerator();

        int opcion;
        do {
            imprimirMenu();
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> agregar(scanner, manager);
                case 2 -> buscar(scanner, manager);
                case 3 -> actualizar(scanner, manager);
                case 4 -> eliminar(scanner, manager);
                case 5 -> validar(scanner, validator);
                case 6 -> generar(scanner, generator);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion no valida");
            }
        } while (opcion != 0);

        scanner.close();
    }

    private static void imprimirMenu() {
        System.out.println();
        System.out.println("===== SecureVault =====");
        System.out.println("1. Agregar credencial");
        System.out.println("2. Buscar credencial");
        System.out.println("3. Actualizar contrasena");
        System.out.println("4. Eliminar credencial");
        System.out.println("5. Validar contrasena");
        System.out.println("6. Generar contrasena segura");
        System.out.println("0. Salir");
    }

    private static void agregar(Scanner scanner, PasswordManager manager) {
        System.out.print("Servicio: ");
        String servicio = scanner.nextLine();
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contrasena: ");
        String contrasena = scanner.nextLine();

        boolean agregada = manager.addCredential(servicio, usuario, contrasena);
        System.out.println(agregada ? "Credencial agregada." : "El servicio ya existe.");
    }

    private static void buscar(Scanner scanner, PasswordManager manager) {
        System.out.print("Servicio: ");
        String servicio = scanner.nextLine();
        Optional<Credential> encontrada = manager.findCredential(servicio);
        if (encontrada.isPresent()) {
            Credential c = encontrada.get();
            System.out.println("Servicio: " + c.servicio());
            System.out.println("Usuario: " + c.usuario());
            System.out.println("Contrasena: " + c.contraseña());
        } else {
            System.out.println("No se encontro la credencial.");
        }
    }

    private static void actualizar(Scanner scanner, PasswordManager manager) {
        System.out.print("Servicio: ");
        String servicio = scanner.nextLine();
        System.out.print("Nueva contrasena: ");
        String nueva = scanner.nextLine();
        boolean actualizada = manager.updatePassword(servicio, nueva);
        System.out.println(actualizada ? "Contrasena actualizada." : "No se encontro la credencial.");
    }

    private static void eliminar(Scanner scanner, PasswordManager manager) {
        System.out.print("Servicio: ");
        String servicio = scanner.nextLine();
        boolean eliminada = manager.deleteCredential(servicio);
        System.out.println(eliminada ? "Credencial eliminada." : "No se encontro la credencial.");
    }

    private static void validar(Scanner scanner, PasswordValidator validator) {
        System.out.print("Contrasena: ");
        String contrasena = scanner.nextLine();

        Resultado resultado = validator.validate(contrasena);
        System.out.println();
        System.out.println("Pruebas:");
        System.out.println("-----------------");
        System.out.println("Longitud:    " + contrasena.length());
        System.out.println("Mayusculas:  " + (PasswordValidator.contieneMayuscula(contrasena) ? "Si" : "No"));
        System.out.println("Minusculas:  " + (PasswordValidator.contieneMinuscula(contrasena) ? "Si" : "No"));
        System.out.println("Numeros:     " + (PasswordValidator.contieneNumero(contrasena) ? "Si" : "No"));
        System.out.println("Especiales:  " + (PasswordValidator.contieneEspecial(contrasena) ? "Si" : "No"));
        System.out.println("Puntuacion:  " + resultado.puntuacion() + "/" + resultado.puntuacionMaxima());
        System.out.println("Nivel:       " + resultado.nivel());
    }

    private static void generar(Scanner scanner, PasswordGenerator generator) {
        System.out.print("Longitud: ");
        int longitud = scanner.nextInt();
        scanner.nextLine();
        String contrasena = generator.generar(longitud);
        System.out.println("Contrasena generada: " + contrasena);
    }
}
