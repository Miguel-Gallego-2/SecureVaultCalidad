package co.edu.udea.securevault.store;

import co.edu.udea.securevault.model.Credential;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class MapCredentialStore implements CredentialStore {

    private final Map<String, Credential> credentials = new LinkedHashMap<>();

    @Override
    public void add(Credential credential) {
        credentials.put(credential.servicio(), credential);
    }

    @Override
    public Optional<Credential> find(String servicio) {
        return Optional.ofNullable(credentials.get(servicio));
    }

    @Override
    public boolean update(String servicio, String nuevaContraseña) {
        Credential credential = credentials.get(servicio);
        if (credential == null) {
            return false;
        }
        credentials.put(servicio, new Credential(credential.servicio(), credential.usuario(), nuevaContraseña));
        return true;
    }

    @Override
    public boolean delete(String servicio) {
        return credentials.remove(servicio) != null;
    }

    @Override
    public int size() {
        return credentials.size();
    }
}
