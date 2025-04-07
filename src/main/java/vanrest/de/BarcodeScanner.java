package vanrest.de;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
@Getter
public class BarcodeScanner implements Runnable {

    private List<Parcel> parcels;
    private List<Parcel> scannedParcels;

    public BarcodeScanner(List<Parcel> parcels) {
        this.parcels = parcels;
        scannedParcels = new ArrayList<>();
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
        Optional<Parcel> parcelOpt = parcels.stream()
                .filter(p -> p.getTrackingNumber().equalsIgnoreCase(scannedLine))
                .findFirst();
        if (parcelOpt.isPresent()) {
            Parcel parcel = parcelOpt.get();
            scannedParcels.add(parcel);
            log.info("The parcel scanned - {}", parcel);
            parcels.remove(parcel);
            scannedParcels.forEach(System.out::println);  //для видалення
        } else {
            Optional<Parcel> parcelOpt2 = scannedParcels.stream()
                    .filter(p -> p.getTrackingNumber().equalsIgnoreCase(scannedLine))
                    .findFirst();
            if (parcelOpt2.isPresent()) {
                System.out.println("The parcel has already been scanned!");
                log.info("The parcel has already been scanned! {}", parcelOpt2.get());
            } else {
                System.out.println("Parcel not found");
                log.warn("Parcel not found {}", scannedLine);
            }
        }
    }
}
