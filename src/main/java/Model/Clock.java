package Model;

import Util.ClockInterface;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Clock implements ClockInterface, ActionListener {

    private int hours;
    private int minutes;
    private int seconds;

    @Override
    public int getHours() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getMinutes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSeconds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setHour(int hours) {
        this.hours = hours;
    }

    @Override
    public void setMinute(int minute) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSecond(int second) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.seconds += 1;
        if (this.seconds == 60) {
            this.seconds = 0;
            this.minutes += 1;
        }
        if (this.minutes == 60) {
            this.minutes = 0;
            this.hours += 1;
        }
        if (this.hours == 24) {
            this.hours = 0;
        }
    }

    @Override
    public String getTime() {
        return new Time().time(this.hours, this.minutes, this.seconds);
    }
}
