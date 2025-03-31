package vanrest.de.dao;


import vanrest.de.model.Parcel;

import java.util.List;

public interface ParcelRepository {

    /**
     * Виведення всієї інформації про невідскановані посилки, а саме ті що додаються з листа
     * @return List of all unchecking parcel
     */
    List<Parcel> getAllParcel();

    /**
     * Adds a new booking after checking for conflicts.
     *
     * @param newParcel The parcel to be added.
     */
    void addParcel (Parcel newParcel);



}
