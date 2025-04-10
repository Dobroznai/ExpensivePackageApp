package de.vanrest.readers;

import de.vanrest.model.Parcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BarcodeScanner implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(BarcodeScanner.class);

    private List<Parcel> parcels;
    private List<Parcel> scannedParcels;

    public BarcodeScanner(List<Parcel> parcels, List<Parcel> scannedParcels) {
        this.parcels = parcels;
        this.scannedParcels = scannedParcels;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextLine()) {
                String scannedLine = scanner.nextLine().trim();
                addParcel(scannedLine);
            }
        }
    }

    private void addParcel(String scannedLine) {
        Optional<Parcel> parcelOpt = findParcel(parcels, scannedLine);
        if (parcelOpt.isPresent()) {
            Parcel parcel = parcelOpt.get();
            scannedParcels.add(parcel);
            log.info("The parcel scanned - {}", parcel);
            parcels.remove(parcel);
        } else {
            Optional<Parcel> parcelOpt2 = findParcel(scannedParcels, scannedLine);
            if (parcelOpt2.isPresent()) {
                System.out.println("The parcel has already been scanned!");
                log.info("The parcel has already been scanned! {}", parcelOpt2.get());
            } else {
                System.out.println("Parcel not found");
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
