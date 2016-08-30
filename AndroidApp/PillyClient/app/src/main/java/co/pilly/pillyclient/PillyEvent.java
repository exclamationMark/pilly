package co.pilly.pillyclient;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class PillyEvent {

    public PillyEvent(long timestamp, int pillDelta, int pillCount, int minutesFromSchedule, boolean handled) {
        this.timestamp = timestamp;
        this.pillDelta = pillDelta;
        this.pillCount = pillCount;
        this.minutesFromSchedule = minutesFromSchedule;
        this.handled = handled;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setPillDelta(int pillDelta) {
        this.pillDelta = pillDelta;
    }

    public void setPillCount(int pillNumber) {
        this.pillCount = pillNumber;
    }

    public void setMinutesFromSchedule(int minutesFromSchedule) {
        this.minutesFromSchedule = minutesFromSchedule;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getPillDelta() {
        return pillDelta;
    }

    public int getPillCount() {
        return pillCount;
    }

    public int getMinutesFromSchedule() {
        return minutesFromSchedule;
    }

    public boolean isHandled() {
        return handled;
    }

    public static PillyEvent fromString(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            long timestamp = jsonObject.getLong("time");
            int pillDelta = jsonObject.getInt("pillDelta");
            int pillCount = jsonObject.getInt("pillCount");
            Object minutesFromSchedule = jsonObject.get("minutesFromSchedule");
            if (minutesFromSchedule instanceof String)
                return new PillyEvent(timestamp, pillDelta, pillCount, 0, false);
            else if (minutesFromSchedule instanceof Integer)
                return new PillyEvent(timestamp, pillDelta, pillCount, (Integer)minutesFromSchedule, true);
            else {
                Log.e("PillyEvent", "Error in reading \"minutesFromSchedule\"");
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        stringBuilder.append("\"minutesFromSchedule\":");
        if(handled)
            stringBuilder.append(minutesFromSchedule);
        else
            stringBuilder.append("N/D");
        stringBuilder.append(",\"pillCount\":");
        stringBuilder.append(pillCount);
        stringBuilder.append(",\"pillDelta\":");
        stringBuilder.append(pillDelta);
        stringBuilder.append(",\"time\":");
        stringBuilder.append(timestamp);
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    long timestamp;
    int pillDelta;
    int pillCount;
    int minutesFromSchedule;
    boolean handled;
}
