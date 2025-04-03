package vanrest.de.readers;

import lombok.Getter;

import java.util.Scanner;

@Getter
public class BarcodeScanner implements Runnable {
    private String inputData;

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (scanner.hasNextLine()) {
                 inputData = scanner.nextLine();
            }
        }
    }
}
