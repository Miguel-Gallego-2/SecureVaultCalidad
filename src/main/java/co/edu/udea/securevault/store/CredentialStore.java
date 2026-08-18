package co.edu.udea.securevault.store;

import co.edu.udea.securevault.model.Credential;

import java.util.Optional;

public interface CredentialStore {

    void add(Credential credential);

    Optional<Credential> find(String servicio);

    boolean update(String servicio, String nuevaContraseña);

    boolean delete(String servicio);

    int size();
}
