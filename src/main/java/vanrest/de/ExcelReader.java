package vanrest.de;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelReader {

    public List<Parcel> read(String filePath) {
        List<Parcel> parcels = new ArrayList<>();

        try (InputStream stream = Files.newInputStream(Path.of(filePath));
             Workbook workbook = new XSSFWorkbook(stream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                String trackingNumber = getCellAsString(row, 0);
                String gibitNumber = getCellAsString(row, 1);
                String tourNumber = getCellAsString(row, 2);

                if (trackingNumber.isEmpty() || gibitNumber.isEmpty() || tourNumber.isEmpty())
                    continue;

                Parcel parcel = new Parcel(trackingNumber, gibitNumber, tourNumber);
                parcels.add(parcel);
            }
        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
            log.error(e.getMessage());
        }
        return parcels;
    }

    private String getCellAsString(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }
}
