package Observer;


import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotificationObserver {
    FBView fb;

    public NotificationObserver(){
        fb = new FBView();
    }

    public void update(String name){

        fb.showNewEvent(name);

    }
}
