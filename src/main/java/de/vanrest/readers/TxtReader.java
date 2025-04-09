package de.vanrest.readers;

import de.vanrest.model.Parcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TxtReader {

    private static final Logger log = LoggerFactory.getLogger(TxtReader.class);

    public List<Parcel> read(String filePath) {
        List<Parcel> parcels = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));

            for (String line : lines) {
                String[] parts = line.split("\\s+");

                if (parts.length != 3)
                    continue;

                String trackingNumber = parts[0];
                String gibitNumber = parts[1];
                String tourNumber = parts[2];

                Parcel parcel = new Parcel(trackingNumber, gibitNumber, tourNumber);
                if (!parcels.contains(parcel))
                    parcels.add(parcel);
                else
                    log.warn("Duplicate - {}", parcel);
            }

        } catch (IOException e) {
            System.out.println("File reading error: " + e.getMessage());
            log.error(e.getMessage());
        }
        return parcels;
    }
}
