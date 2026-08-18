package co.edu.udea.securevault.service;

import co.edu.udea.securevault.model.Credential;
import co.edu.udea.securevault.store.CredentialStore;

import java.util.Optional;

public final class PasswordManager {

    private final CredentialStore store;

    public PasswordManager(CredentialStore store) {
        this.store = store;
    }

    public boolean addCredential(String servicio, String usuario, String contraseña) {
        if (store.find(servicio).isPresent()) {
            return false;
        }
        store.add(new Credential(servicio, usuario, contraseña));
        return true;
    }

    public Optional<Credential> findCredential(String servicio) {
        return store.find(servicio);
    }

    public boolean updatePassword(String servicio, String nuevaContraseña) {
        return store.update(servicio, nuevaContraseña);
    }

    public boolean deleteCredential(String servicio) {
        return store.delete(servicio);
    }

    public int size() {
        return store.size();
    }
}
