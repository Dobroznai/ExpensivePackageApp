package vanrest.de.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import vanrest.de.utilities.ParcelStatus;

import java.time.format.DateTimeFormatter;

@Getter
@EqualsAndHashCode(of = {"trackingNumber"})
public class Parcel {
    private static int nextId = 1;

    private final int idParcel;
    private final String trackingNumber;
    private final int tourNumber;
    private final String gibitNumber;

    // створено на випадку перевірки термінів по посилкам.
    // private final String terminDate;
    // private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final ParcelStatus status;

    public Parcel(int idParcel, String trackingNumber, int tourNumber, String gibitNumber, String terminDate, ParcelStatus status) {

        this.idParcel = nextId++;
        this.trackingNumber = trackingNumber;
        this.tourNumber = tourNumber;
        this.gibitNumber = gibitNumber;

        //this.terminDate = terminDate;
        this.status = status;
        //dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Checking{");
        sb.append("idParcel=").append(idParcel);
        sb.append(", trackingNumber='").append(trackingNumber);
        sb.append(", tourNumber=").append(tourNumber);
        sb.append(", gibitNumber='").append(gibitNumber);
        // sb.append(", terminDate='").append(String.format(terminDate, dateFormatter));
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
