import javax.sound.midi.Soundbank;
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

    static void showRestaurants(int id, Statement st, ResultSet rs){
        System.out.println("Restaurants:");
        try {
            rs=st.executeQuery("SELECT * FROM restaurant WHERE managerID="+id);
            while(rs.next()){
                String str=rs.getString("name");
                System.out.println(str);
            }
        } catch (SQLException e) {
            System.err.println("showRestaurant has error");
            throw new RuntimeException(e);
        }
    }
    static void addRestaurant(String name, int x, int y, String foodType, )
    //driver code
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Scanner input = new Scanner(System.in);
        ArrayList<Manager> managers=new ArrayList<>();
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/database";
        Connection con = DriverManager.getConnection(url, "root", "Bale2004");
        Statement st = con.createStatement();
        int phase=0; //login and register
        String command = input.nextLine();
        String user = "", pass= "";
        if(phase==0) {
            switch (command) {
                case "add manager":
                    user = input.next();
                    pass = input.next();
                    //managers.add(new Manager(1,user,pass));
                    System.out.println("Welcome Manager." + user + "!");
                    //System.out.println(managers.get(0).getUserName());
                    st.executeUpdate("INSERT INTO manager(userName,password) VALUES " + "(\"" + user + "\",\"" + pass + "\")");
                    phase=1; //restaurant list
                    break;
                case "add customer":
                    user = input.next();
                    pass = input.next();
                    System.out.println("Welcome Customer." + user + "!");
                    st.executeUpdate("INSERT INTO customer(userName,password) VALUES " + "(\"" + user + "\",\"" + pass + "\")");
                case "login manager":

                case "login customer":
            }
        }
        if(phase==1){
            ResultSet rs=st.executeQuery("SELECT * FROM manager WHERE userName="+"\'"+user+"\'");
            int id=0;
            while(rs.next()) {
                id = rs.getInt("managerID");
            }
            showRestaurants(id,st,rs);
        }
    }
}


