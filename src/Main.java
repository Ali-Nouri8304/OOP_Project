import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    /*Timer timer = new Timer();

    Main(int seconds) {
        timer.schedule(new RemindTask(), seconds * 1000);
    }

    class RemindTask extends TimerTask {
        public void run() {
            System.out.println("You have a notification!");
            //terminate the timer thread
            timer.cancel();
        }
    }*/

    //driver code
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Scanner input = new Scanner(System.in);
        ArrayList<Manager> managers=new ArrayList<>();
        /*System.out.println(1);
        new Main(2);
        System.out.println(2);
        new Main(3);*/
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/database";
            Connection con = DriverManager.getConnection(url, "root", "Bale2004");
            Statement st = con.createStatement();

            //con.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        //Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/database";
        Connection con = DriverManager.getConnection(url, "root", "Bale2004");
        Statement st = con.createStatement();

        String command = input.nextLine();
        String user,pass;
        switch (command){
            case "add manager":
                user=input.next();
                pass= input.next();
                managers.add(new Manager(1,user,pass));
                System.out.println("Welcome " + user + "!");
                System.out.println(managers.get(0).getUserName());
                st.executeUpdate("INSERT INTO manager(userName,password) VALUES (user,pass)");
            case "add customer":

            case "login manager":

            case "login customer":
        }
    }
}


