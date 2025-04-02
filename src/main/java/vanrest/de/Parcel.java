package vanrest.de;

public class Parcel {
    private static int nextId = 1;
    private final int idParcel;
    private final String trackingNumber;
    private final String  tourNumber;
    private final String gibitNumber;

    public Parcel(String trackingNumber, String tourNumber, String gibitNumber) {
        this.idParcel = nextId++;
        this.trackingNumber = trackingNumber;
        this.tourNumber = tourNumber;
        this.gibitNumber = gibitNumber;
    }

    @Override
    public String toString() {
        return "Parcel{" +
                "idParcel=" + idParcel +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", tourNumber='" + tourNumber + '\'' +
                ", gibitNumber='" + gibitNumber + '\'' +
                '}';
    }
}
