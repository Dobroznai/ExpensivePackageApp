package de.vanrest.models;

import java.util.Objects;

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

    public int getIdParcel() {return idParcel;}
    public String getTrackingNumber() {return trackingNumber;}
    public String getGibitNumber() {return gibitNumber;}
    public String getTourNumber() {return tourNumber;}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return Objects.equals(trackingNumber, parcel.trackingNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(trackingNumber);
    }
}
