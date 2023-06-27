import java.util.Timer;
import java.util.TimerTask;

public class Main {
    Timer timer = new Timer();
    Main(int seconds) {
        timer.schedule(new RemindTask(), seconds*1000);
    }
    class RemindTask extends TimerTask {
        public void run() {
            System.out.println("You have a notification!");
    //terminate the timer thread
            timer.cancel();
        }
    }
    //driver code
    public static void main(String args[]) {
        System.out.println(1);
        new Main(2);
        System.out.println(2);
        new Main(3);
    }
}



