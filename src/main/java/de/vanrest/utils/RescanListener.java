package de.vanrest.utils;

import de.vanrest.models.Parcel;

public interface RescanListener {
    void onRepeatedScan(Parcel parcel);
}
