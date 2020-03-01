package Util;

public interface ClockInterface {

    public int getHours();

    public int getMinutes();

    public int getSeconds();

    public void setHour(int hours);

    public void setMinute(int minute);

    public void setSecond(int second);

    public String getTime();
}