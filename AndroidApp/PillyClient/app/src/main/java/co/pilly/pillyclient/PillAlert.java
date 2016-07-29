package co.pilly.pillyclient;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

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

    public long getNextTrigger() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Arrays.sort(days); // TODO: check if needed
        if (Arrays.asList(days).contains(calendar.get(Calendar.DAY_OF_WEEK))) {
            if (calendar.get(Calendar.HOUR_OF_DAY)*100+calendar.get(Calendar.MINUTE) < hours*100 + minutes) {
                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minutes);
                return calendar.getTimeInMillis();
            }
        }
        int i;
        for (i = 0; i < days.length; i++)
            if (days[i] > calendar.get(Calendar.DAY_OF_WEEK))
                break;
        if(i < days.length) {
            calendar.add(Calendar.DAY_OF_MONTH, days[i] - calendar.get(Calendar.DAY_OF_WEEK));
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            return calendar.getTimeInMillis();
        }
        calendar.add(Calendar.DAY_OF_MONTH, days[0] + 7 - calendar.get(Calendar.DAY_OF_WEEK));
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        return calendar.getTimeInMillis();
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

    // Override toString() so it returns a JSON describing the PillAlert object content

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

    // Create a PillAlert object from a JSON string

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
