package co.edu.udea.securevault.store;

import co.edu.udea.securevault.model.Credential;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ListCredentialStore implements CredentialStore {

    private final List<Credential> credentials = new ArrayList<>();

    @Override
    public void add(Credential credential) {
        credentials.add(credential);
    }

    @Override
    public Optional<Credential> find(String servicio) {
        for (Credential credential : credentials) {
            if (credential.servicio().equals(servicio)) {
                return Optional.of(credential);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean update(String servicio, String nuevaContraseña) {
        for (int i = 0; i < credentials.size(); i++) {
            Credential credential = credentials.get(i);
            if (credential.servicio().equals(servicio)) {
                credentials.set(i, new Credential(credential.servicio(), credential.usuario(), nuevaContraseña));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String servicio) {
        return credentials.removeIf(credential -> credential.servicio().equals(servicio));
    }

    @Override
    public int size() {
        return credentials.size();
    }
}
