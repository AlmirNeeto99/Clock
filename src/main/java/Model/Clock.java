package Model;

import Util.ClockInterface;
import java.util.Random;

public class Clock implements ClockInterface {

    private int hours;
    private int minutes;
    private int seconds;

    public Clock() {
        Random ran = new Random();
        setHour(ran.nextInt(24));
        setMinute(ran.nextInt(60));
        setSecond(ran.nextInt(60));
    }

    @Override
    public int getHours() {
        return this.hours;
    }

    @Override
    public int getMinutes() {
        return this.minutes;
    }

    @Override
    public int getSeconds() {
        return this.seconds;
    }

    @Override
    public void setHour(int hours) {
        if (hours >= 24) {
            this.hours = 0;
        } else {
            this.hours = hours;
        }
    }

    @Override
    public void setMinute(int minutes) {
        if (minutes >= 60) {
            this.minutes = 0;
        } else {
            this.minutes = minutes;
        }
    }

    @Override
    public void setSecond(int seconds) {
        if (seconds >= 60) {
            this.seconds = 0;
        } else {
            this.seconds = seconds;
        }
    }

    public void att() {
        this.seconds += 1;
        if (this.seconds >= 60) {
            this.seconds = 0;
            this.minutes += 1;
        }
        if (this.minutes >= 60) {
            this.minutes = 0;
            this.hours += 1;
        }
        if (this.hours >= 24) {
            this.hours = 0;
        }
    }

    @Override
    public String getTime() {
        return new Time().time(this.hours, this.minutes, this.seconds);
    }
}
