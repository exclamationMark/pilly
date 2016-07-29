package co.pilly.pillyclient;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PillAlert implements Parcelable, Comparable<PillAlert> {
    private int hours;
    private int minutes;
    private int quantity;
    private int[] days;

    public PillAlert(int hours, int minutes, int quantity, int[] days) {
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

    public int[] getDays() {
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
        int thisValue = this.getHours() * 100 + this.getMinutes();
        int thatValue = o.getHours() * 100 + o.getMinutes();
        return thisValue - thatValue;
    }

    public String toString() {
        StringBuilder stringBuffer = new StringBuilder("{");
        stringBuffer.append("\"hourOfDay\" : ");
        stringBuffer.append(hours);
        stringBuffer.append(" , \"minutes\" : ");
        stringBuffer.append(minutes);
        stringBuffer.append(", \"quantity\" : ");
        stringBuffer.append(quantity);
        stringBuffer.append(", \"days\" : [");
        for(int i = 0; i < days.length; i++) {
            stringBuffer.append(days[i]);
            stringBuffer.append(",");
        }
        stringBuffer.deleteCharAt(stringBuffer.length()-1);
        stringBuffer.append("]}");

        return stringBuffer.toString();
    }

    public static PillAlert fromString(String JSON) {
        JSONObject jsonObject;

        if(JSON == null)
            return null;

        try {
            jsonObject = new JSONObject(JSON);
            JSONArray jdaysArray = jsonObject.getJSONArray("days");

            int h,m,q;
            int[] d = new int[jdaysArray.length()];

            h = jsonObject.getInt("hourOfDay");
            m = jsonObject.getInt("minutes");
            q = jsonObject.getInt("quantity");
            for(int i = 0; i < jdaysArray.length(); i++)
                d[i] = jdaysArray.getInt(i);

            return new PillAlert(h, m, q, d);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
