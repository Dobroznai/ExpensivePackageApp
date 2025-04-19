package de.vanrest.readers;

import de.vanrest.controllers.TourNumberController;
import de.vanrest.utils.RescanListener;
import de.vanrest.models.Parcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class BarcodeScanner {

    private static final Logger log = LoggerFactory.getLogger(BarcodeScanner.class);
    private RescanListener rescanListener;
    private final List<Parcel> parcels;
    private final List<Parcel> scannedParcels;

    public BarcodeScanner(List<Parcel> parcels, List<Parcel> scannedParcels) {
        this.parcels = parcels;
        this.scannedParcels = scannedParcels;
    }

    public void setRescanListener(RescanListener rescanListener) {this.rescanListener = rescanListener;}

    public void addParcel(String scannedLine) {
        Optional<Parcel> parcelOpt = findParcel(parcels, scannedLine);
        if (parcelOpt.isPresent()) {
            Parcel parcel = parcelOpt.get();
            scannedParcels.add(parcel);
            TourNumberController.show(parcel.getTourNumber());
            log.info("The parcel scanned - {}", parcel.getTrackingNumber());
            parcels.remove(parcel);
        } else {
            Optional<Parcel> parcelOpt2 = findParcel(scannedParcels, scannedLine);
            if (parcelOpt2.isPresent()) {
                rescanListener.onRepeatedScan(parcelOpt2.get());
                log.info("The parcel has already been scanned - {}", parcelOpt2.get().getTrackingNumber());
            } else {
                log.warn("Parcel not found {}", scannedLine);
            }
        }
    }

    private Optional<Parcel> findParcel(List<Parcel> parcels, String trackingNumber) {
        return parcels.stream()
                .filter(parcel -> parcel.getTrackingNumber().equalsIgnoreCase(trackingNumber))
                .findFirst();
    }
}
