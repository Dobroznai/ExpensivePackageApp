package de.vanrest.readers;

import de.vanrest.model.Parcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    private static final Logger log = LoggerFactory.getLogger(ExcelReader.class);

    public List<Parcel> read(String filePath) {
        List<Parcel> parcels = new ArrayList<>();

        try (InputStream inStream = Files.newInputStream(Path.of(filePath));
             Workbook workbook = new XSSFWorkbook(inStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                String trackingNumber = getCellAsString(row, 0);
                String gibitNumber = getCellAsString(row, 1);
                String tourNumber = getCellAsString(row, 2);

                if (trackingNumber.isEmpty() || gibitNumber.isEmpty() || tourNumber.isEmpty())
                    continue;

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

    private String getCellAsString(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }
}
