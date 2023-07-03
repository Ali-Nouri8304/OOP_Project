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

    static void showRestaurants(int managerID, Statement st, ResultSet rs) {
        System.out.println("Restaurants:");
        try {
            rs = st.executeQuery("SELECT * FROM restaurant WHERE managerID=" + managerID);
            while (rs.next()) {
                String str = rs.getString("name");
                System.out.println(str);
            }
        } catch (SQLException e) {
            System.err.println("showRestaurant has error");
            throw new RuntimeException(e);
        }
    }

    static void addRestaurant(String Name, int x, int y, String foodType, int managerID, Statement st) {
        try {
            st.executeUpdate("INSERT INTO restaurant(managerID,foodType,x,y,name) VALUES " +
                    "(" + managerID + ",\"" + foodType + "\"," + x + "," + y + ",\"" + Name + "\")");
        } catch (SQLException e) {
            System.err.println("addRestaurant has error");
            throw new RuntimeException(e);
        }
    }

    static void addFood(String Name, double price, int resID, Statement st) {
        try {
            st.executeUpdate("INSERT INTO food(name,price,discount,resID,active,ongoingOrders) VALUES(\""
                    + Name + "\"," + price + "," + 0 + "," + resID + "," + 0 + "," + 0 + ")");
        } catch (SQLException e) {
            System.err.println("addFood has error");
            throw new RuntimeException(e);
        }
    }

    static void showMenu(int resID, Statement st, ResultSet rs) {
        System.out.println("Menu:");
        try {
            rs = st.executeQuery("SELECT * FROM food WHERE resID=" + resID);
            while (rs.next()) {
                int id = rs.getInt("foodID");
                String str = rs.getString("name");
                double price = rs.getDouble("price");
                boolean active = rs.getBoolean("active");
                int discount = rs.getInt("discount");
                System.out.println(id + "\t" + str + "\t" + price + "\t" + active + "\t" + discount + "%");
            }
        } catch (SQLException e) {
            System.err.println("showRestaurant has error");
            throw new RuntimeException(e);
        }
    }
    static void showComments(int id, Statement st, ResultSet rs) throws SQLException {
        rs = st.executeQuery("SELECT * FROM comment WHERE foodID=" + id);
        String comment ,response;
        System.out.println("comments:");
        while (rs.next()) {
             comment= rs.getString("comment");
             response=rs.getString("response");
            if(!comment.equals("")) {
                System.out.print(comment + "\t");
                if (!response.equals("")) System.out.println("response: " + response);
                else System.out.println("No response");
            }
        }
    }

    //driver code
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        Scanner input = new Scanner(System.in);
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/database";
        Connection con = DriverManager.getConnection(url, "root", "Bale2004");
        Statement st = con.createStatement();
        ResultSet rs = null;
        int phase = 0; //login and register
        String command = "";
        String user = "", pass = "";
        while (true) {
            while (phase == 0) {
                System.out.println("login/register");
                command = input.nextLine();
                switch (command) {
                    case "add manager":
                        user = input.next();
                        pass = input.next();
                        System.out.println("Welcome Manager." + user + "!");
                        st.executeUpdate("INSERT INTO manager(userName,password) VALUES " + "(\"" + user + "\",\"" + pass + "\")");
                        phase = 1; //manager's page
                        break;
                    case "add customer":
                        user = input.next();
                        pass = input.next();
                        System.out.println("Welcome Customer." + user + "!");
                        System.out.println("Please enter x, y, balance!");
                        int x = input.nextInt();
                        int y = input.nextInt();
                        double balance = input.nextDouble();
                        st.executeUpdate("INSERT INTO customer(userName,password,x,y,balance) VALUES(\""
                                + user + "\",\"" + pass + "\"," + x + "," + y + "," + balance + ")");
                        phase = 4; //customer's page
                    case "login manager":

                    case "login customer":
                }
            }
            int manID = 0, resID = 0;

            while (phase == 1) {
                rs = st.executeQuery("SELECT * FROM manager WHERE userName=" + "\"" + user + "\"");
                while (rs.next()) {
                    manID = rs.getInt("managerID");
                }
                showRestaurants(manID, st, rs);
                command = input.nextLine();
                if (command.equals("add restaurant")) {
                    System.out.println("Please enter restaurant name, x, y, food type!");
                    String name = input.next();
                    int x = input.nextInt();
                    int y = input.nextInt();
                    String foodType = input.next();
                    addRestaurant(name, x, y, foodType, manID, st);
                    rs = st.executeQuery("SELECT * FROM restaurant WHERE managerID=" + manID + " AND name=" + "\"" + name + "\"");
                    while (rs.next()) {
                        resID = rs.getInt("resID");
                    }
                    phase = 2; //restaurant page
                }
                if (command.equals("select restaurant")) {
                    phase = 2; //restaurant page
                }
            }
            while (phase == 2) {
                System.out.println(rs.getString("name"));
                command = input.nextLine();
                if (command.equals("show menu")) {
                    showMenu(resID, st, rs);
                    command = input.nextLine();
                    if (command.equals("add food")) {
                        System.out.println("Please add food name, price!");
                        while (!input.next().equals("end")) {
                            String name = input.next();
                            Double price = input.nextDouble();
                            addFood(name, price, resID, st);
                        }
                    }
                    if (command.equals("edit food")) {
                        System.out.println("Enter the food ID you want to edit!");
                        int id = input.nextInt();
                        System.out.println("Name or Price?");
                        String str = input.next();
                        if (str.equals("name")) {
                            String New = input.next();
                            st.executeUpdate("UPDATE food SET name=\'" + New + "\' WHERE foodID=" + id + ")");
                        } else {
                            double New = input.nextDouble();
                            st.executeUpdate("UPDATE food SET price=" + New + " WHERE foodID=" + id + ")");
                        }
                    }
                    if (command.equals("delete food")) {
                        System.out.println("Enter the food ID you want to delete!");
                        int id = input.nextInt();
                        System.out.println("Are you sure you want to delete this food ID?");
                        String confirmation = input.next();
                        if (confirmation.equals("yes")) {
                            rs = st.executeQuery("SELECT * FROM food WHERE foodID=" + id);
                            int onOrd = 1;
                            while (rs.next()) {
                                onOrd = rs.getInt("ongoingOrders");
                            }
                            if (onOrd != 0) {
                                System.out.println("You can not delete the selected food item");
                            } else {
                                st.executeUpdate("DELETE FROM food WHERE foodID=" + id);
                                System.out.println("Food item deleted:((((");
                            }
                        } else {
                            System.out.println("Canceled deleting");
                        }
                    }
                    if (command.equals("activate food")) {
                        System.out.println("Enter the food ID you want to active!");
                        int id = input.nextInt();
                        st.executeUpdate("UPDATE food SET active=1");
                        System.out.println("Food item activated:))))");
                    }
                    if (command.equals("deactivate food")) {
                        System.out.println("Enter the food ID you want to deactivate!");
                        int id = input.nextInt();
                        System.out.println("Are you sure you want to deactivate this food ID?");
                        String confirmation = input.next();
                        if (confirmation.equals("yes")) {
                            rs = st.executeQuery("SELECT * FROM food WHERE foodID=" + id);
                            int onOrd = 1;
                            while (rs.next()) {
                                onOrd = rs.getInt("ongoingOrders");
                            }
                            if (onOrd != 0) {
                                System.out.println("You can not deactivate the selected food item");
                            } else {
                                st.executeUpdate("UPDATE food SET active=0");
                                System.out.println("Food item deactivated:((((");
                            }
                        } else {
                            System.out.println("Canceled deactivating");
                        }
                    }
                    if (command.equals("select food")) {
                        System.out.println("Enter the food ID you want to select");
                        int id=input.nextInt();
                        String com=input.nextLine();
                        if (com.equals("show comments")) {
                            showComments(id,st,rs);
                        }
                        if (com.equals("add response")) {

                        }
                        if (com.equals("edit response")) {

                        }
                        if (com.equals("show ratings")) {

                        }
                    }
                }
                if (command.equals("show location")) {

                }
                if (command.equals("show food type")) {

                }
                if (command.equals("edit food type")) {

                }
                if (command.equals("show open orders")) {
                    if (command.equals("edit order")) {

                    }
                    if (command.equals("show order details")) {

                    }
                }
                if (command.equals("show order history")) {

                }
            }

        }
    }
}


