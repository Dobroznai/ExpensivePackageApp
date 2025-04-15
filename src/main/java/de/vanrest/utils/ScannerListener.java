package de.vanrest.utils;

import de.vanrest.models.Parcel;

public interface ScannerListener {
    void onParcelScanned(Parcel parcel);
}
