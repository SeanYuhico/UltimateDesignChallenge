package ClientControl;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client{

    private Connection c;
    private String user;
    private String directory;
    private boolean isRunning = true;

    public Client(String user, String ip, String directory){
        try {
            this.user = user;
            this.directory = directory;
            Socket s = new Socket(ip, 10000);
            c = new Connection(s, this);
            //c.start();

            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(c);


            //inputListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inputListener(){
        Scanner scan = new Scanner(System.in);
        while(isRunning){
            while(!scan.hasNextLine()){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isRunning = false;
                }
            }
            String str = scan.nextLine();
            if(str.equalsIgnoreCase("exit")){
                break;
            }
            c.sendToServer(str);
        }
        c.close();
    }

    public Connection getConnection(){return c;}

    public String getUser(){
        return user;
    }

    public void setUser(String user){
        this.user = user;
    }

    public String getDirectory(){return directory;}

    public void setDirectory(String directory){this.directory = directory;}
}
