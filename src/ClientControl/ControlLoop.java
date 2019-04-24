package ClientControl;

public class ControlLoop implements Runnable{
    private boolean running=true;
    private int delay = 10000;

    public void run() {
        while(running){
            //collect files code
            ClientController.getInstance().getMusic();

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //database sync
            ClientController.getInstance().syncAll();

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void end(){
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
