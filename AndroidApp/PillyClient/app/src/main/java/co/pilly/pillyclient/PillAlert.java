package co.pilly.pillyclient;

public class PillAlert {
    private int hours;
    private int minutes;
    private int quantity;
    private DAYS [] days;

    public PillAlert(int hours, int minutes, int quantity, DAYS [] days) {
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

    public DAYS [] getDays() {
        return days;
    }

    public void setDays(DAYS[] days) {
        this.days = days;
    }
}
