package Model;

public final class Time {

    public static String time(int hours, int minutes, int seconds) {
        String ret = "Now: ";
        if (hours < 10) {
            ret += "0";
        }
        ret += hours + ":";
        if (minutes < 10) {
            ret += "0";
        }
        ret += minutes + ":";
        if (seconds < 10) {
            ret += "0";
        }
        ret += seconds;
        return ret;
    }
}
