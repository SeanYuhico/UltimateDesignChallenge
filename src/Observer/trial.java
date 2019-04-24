package Observer;

public class trial {
    public static void main(String arg[]){

        NotificationObserver no = new NotificationObserver();

        String c = "yehe";

        no.update(c);
        no.update("sdf");
        no.update("hehe");
    }
}
