package vanrest.de;

public class Parcel {
    private static int nextId = 1;
    private final int idParcel;
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
        return "Parcel{" +
                "idParcel=" + idParcel +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", gibitNumber='" + gibitNumber + '\'' +
                ", tourNumber='" + tourNumber + '\'' +
                '}';
    }
}
