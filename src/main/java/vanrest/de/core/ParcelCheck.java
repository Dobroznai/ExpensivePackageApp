package vanrest.de.core;

import vanrest.de.dao.ParcelRepository;
import vanrest.de.dao.FileStorage;
import vanrest.de.model.Parcel;

import java.util.List;

public class ParcelCheck {
    private final ParcelRepository repository;

    public ParcelCheck(ParcelRepository repository) {
        this.repository = repository;
        loadParcels(); // Load parcels from file on startup
    }

    private void loadParcels() {
        List<Parcel> loadedParcel = FileStorage.loadFromFile();
    }
}
