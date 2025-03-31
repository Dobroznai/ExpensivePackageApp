package vanrest.de.app;

import vanrest.de.model.Parcel;
import vanrest.de.utilities.ParcelStatus;

public class ConsoleUI {
    public static void main(String[] args) {
        Parcel checking = new Parcel(
                1,
                "H100000888989",
                362,
                "80-18",
                "27.06.25",
                ParcelStatus.DONE);

        // Правильний вивід
        System.out.println(checking);
    }
}