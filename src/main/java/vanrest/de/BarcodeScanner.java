package vanrest.de;

import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;

/**
 *  Сирий код
 *  Так, як сканер має запускаться автоматично при запуску програми і працювать до закриття програми,
 *  то даний код потрібно буде підлаштувать під ці вимоги.
 *  Його реалізацією займемось при написанні керуючого класу
 */

@Getter
@Setter
public class BarcodeScanner {

    private String inputData;

    public String scan(String inputData) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (scanner.hasNextLine())
                inputData = scanner.nextLine();
        }
    }
}
