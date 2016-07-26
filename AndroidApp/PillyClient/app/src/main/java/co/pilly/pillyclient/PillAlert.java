package co.pilly.pillyclient;

import android.os.Parcel;
import android.os.Parcelable;

public class PillAlert implements Parcelable, Comparable<PillAlert> {
    private int hours;
    private int minutes;
    private int quantity;
    private int [] days;

    public PillAlert(int hours, int minutes, int quantity, int [] days) {
        this.hours = hours;
        this.minutes = minutes;
        this.quantity = quantity;
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int [] getDays() {
        return days;
    }

    public void setDays(int[] days) {
        this.days = days;
    }

    // Parcelable overrides

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(hours);
        out.writeInt(minutes);
        out.writeInt(quantity);
        out.writeIntArray(days);
    }

    public static final Parcelable.Creator<PillAlert> CREATOR
            = new Parcelable.Creator<PillAlert>() {
        public PillAlert createFromParcel(Parcel in) {
            return new PillAlert(in);
        }

        public PillAlert[] newArray(int size) {
            return new PillAlert[size];
        }
    };

    private PillAlert(Parcel in) {
        hours = in.readInt();
        minutes = in.readInt();
        quantity = in.readInt();
        days = in.createIntArray();
    }

    // Comparable overrides

    public int compareTo(PillAlert o) {
        int thisValue = this.getHours()*100 + this.getMinutes();
        int thatValue = o.getHours()*100 + o.getMinutes();
        return thisValue - thatValue;
    }
}
