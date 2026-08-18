package co.edu.udea.securevault.service;

import co.edu.udea.securevault.model.Credential;
import co.edu.udea.securevault.store.ListCredentialStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordManagerTest {

    private PasswordManager manager;

    @BeforeEach
    void setUp() {
        // ARRANGE (estado inicial en memoria)
        manager = new PasswordManager(new ListCredentialStore());
    }

    @Test
    void agregarYBuscarCredencial() {
        // ARRANGE
        String servicio = "GitHub";
        String usuario = "usuario123";

        // ACT
        boolean agregada = manager.addCredential(servicio, usuario, "MiP4ssw0rd!");
        Optional<Credential> encontrada = manager.findCredential(servicio);

        // ASSERT
        assertTrue(agregada);
        assertTrue(encontrada.isPresent());
        assertEquals(usuario, encontrada.get().usuario());
    }

    @Test
    void noPermiteAgregarServicioDuplicado() {
        // ARRANGE
        manager.addCredential("GitHub", "usuario123", "MiP4ssw0rd!");

        // ACT
        boolean duplicada = manager.addCredential("GitHub", "otroUsuario", "OtraPass1!");

        // ASSERT
        assertFalse(duplicada);
        assertEquals(1, manager.size());
    }

    @Test
    void actualizarContrasena() {
        // ARRANGE
        manager.addCredential("GitHub", "usuario123", "ViejaPass1!");
        String nueva = "NuevaPass2@";

        // ACT
        boolean actualizada = manager.updatePassword("GitHub", nueva);
        Optional<Credential> encontrada = manager.findCredential("GitHub");

        // ASSERT
        assertTrue(actualizada);
        assertEquals(nueva, encontrada.get().contraseña());
    }

    @Test
    void eliminarCredencial() {
        // ARRANGE
        manager.addCredential("GitHub", "usuario123", "MiP4ssw0rd!");

        // ACT
        boolean eliminada = manager.deleteCredential("GitHub");
        Optional<Credential> encontrada = manager.findCredential("GitHub");

        // ASSERT
        assertTrue(eliminada);
        assertTrue(encontrada.isEmpty());
        assertEquals(0, manager.size());
    }

    @Test
    void buscarServicioInexistenteDevuelveVacio() {
        // ARRANGE
        manager.addCredential("GitHub", "usuario123", "MiP4ssw0rd!");

        // ACT
        Optional<Credential> encontrada = manager.findCredential("Bitbucket");

        // ASSERT
        assertTrue(encontrada.isEmpty());
    }
}
