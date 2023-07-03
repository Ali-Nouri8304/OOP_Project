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
    static void addRestaurant(String Name, int x, int y, String foodType, int managerID, Statement st){
        try {
            st.executeUpdate("INSERT INTO restaurant(managerID,foodType,x,y,name) VALUES " +
                    "(" + managerID + ",\"" + foodType + "\"," + x + "," + y + ",\"" + Name + "\")");
        } catch (SQLException e) {
            System.err.println("addRestaurant has error");
            throw new RuntimeException(e);
        }
    }
    static void addFood(String Name, double price, int resID, Statement st){
        try {
            st.executeUpdate("INSERT INTO food(name,price,discount,resID,active,ongoingOrders) VALUES(\""
                    + Name + "\"," + price + "," + 0 + "," + resID + "," + 0 + "," + 0 + ")");
        } catch (SQLException e) {
            System.err.println("addFood has error");
            throw new RuntimeException(e);
        }
    }
    //driver code
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Scanner input = new Scanner(System.in);
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/database";
        Connection con = DriverManager.getConnection(url, "root", "Bale2004");
        Statement st = con.createStatement();
        int phase = 0; //login and register
        String command = input.nextLine();
        String user = "", pass = "";
        while (true) {
            if (phase == 0) {
                System.out.println("login/register");
                switch (command) {
                    case "add manager":
                        user = input.next();
                        pass = input.next();
                        System.out.println("Welcome Manager." + user + "!");
                        st.executeUpdate("INSERT INTO manager(userName,password) VALUES " + "(\"" + user + "\",\"" + pass + "\")");
                        phase = 1; //add restaurant
                        break;
                    case "add customer":
                        user = input.next();
                        pass = input.next();
                        System.out.println("Welcome Customer." + user + "!");
                        System.out.println("Please enter x, y, balance!");
                        int x=input.nextInt();
                        int y=input.nextInt();
                        double balance=input.nextDouble();
                        st.executeUpdate("INSERT INTO customer(userName,password,x,y,balance) VALUES(\""
                                + user + "\",\"" + pass + "\"," + x + "," + y + "," + balance + ")");
                        phase=4; //customer's page
                    case "login manager":

                    case "login customer":
                }
            }
            int id = 0;
            if (phase == 1) {
                ResultSet rs = st.executeQuery("SELECT * FROM manager WHERE userName=" + "\'" + user + "\'");
                while (rs.next()) {
                    id = rs.getInt("managerID");
                }
                System.out.println("Please enter restaurant name, x, y, food type!");
                String name = input.next();
                int x = input.nextInt();
                int y = input.nextInt();
                String foodType = input.next();
                addRestaurant(name, x, y, foodType, id, st);
                phase = 2; //restaurant page
            }
            if (phase == 2) {
                System.out.println("Please add food name, price!");
                while (!input.next().equals("end")) {
                    String name = input.next();
                    double price = input.nextDouble();
                    addFood(name, price, id, st);
                }
                phase = 5; //manager's page
            }
            if(phase==4){

            }
        }
    }
}


