package vanrest.de;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ExpensivePackageApp {

    public static void main(String[] args) {
        String filePath = "D:\\programming\\Parcels.txt";
        List<Parcel> parcels = getParcels(filePath);

        if (parcels != null)
            parcels.forEach(System.out::println);

        BarcodeScanner barcodeScanner = new BarcodeScanner(parcels);
        Thread scannerThread = new Thread(barcodeScanner);
        scannerThread.start();
    }

    public static List<Parcel> getParcels(String filePath) {
        if (filePath.endsWith(".txt")) {
            return new TxtReader().read(filePath);
        } else if (filePath.endsWith(".xlsx")) {
            return new ExcelReader().read(filePath);
        } else {
            System.out.println("File format is not supported");
            log.error("Input path - {}", filePath);
            return null;
        }
    }
}
