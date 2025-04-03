package vanrest.de.readers;

import lombok.extern.slf4j.Slf4j;
import vanrest.de.Parcel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TxtReader {

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
                parcels.add(parcel);
            }

        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
            log.error(e.getMessage());
        }
        return parcels;
    }
}
