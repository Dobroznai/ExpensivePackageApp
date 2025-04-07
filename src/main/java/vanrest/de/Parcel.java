package vanrest.de;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"trackingNumber"})

public class Parcel {
    private static int nextId = 1;
    final int idParcel;
    private final String trackingNumber;
    private final String gibitNumber;
    private final String tourNumber;

    public Parcel(String trackingNumber, String gibitNumber, String tourNumber) {
        this.idParcel = nextId++;
        this.trackingNumber = trackingNumber;
        this.gibitNumber = gibitNumber;
        this.tourNumber = tourNumber;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Parcel{");
        sb.append("idParcel=").append(idParcel);
        sb.append(", trackingNumber='").append(trackingNumber).append('\'');
        sb.append(", gibitNumber='").append(gibitNumber).append('\'');
        sb.append(", tourNumber='").append(tourNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
