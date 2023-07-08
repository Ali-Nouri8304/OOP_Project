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
                int id = rs.getInt("resID");
                String str = rs.getString("name");
                System.out.println(id + "." + str);
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
                    + Name + "\"," + price + "," + 0 + "," + resID + "," + 1 + "," + 0 + ")");
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
    static void showComments(int foodID, Statement st, ResultSet rs) throws SQLException {
        rs = st.executeQuery("SELECT * FROM comment WHERE foodID=" + foodID);
        String comment ,response;
        System.out.println("comments:");
        while (rs.next()) {
            int ID=rs.getInt("commentID");
            comment= rs.getString("comment");
            response=rs.getString("response");
            if(!comment.equals("")) {
                System.out.print(ID + "." + comment + "\t");
                if (!response.equals("")) System.out.println("response: " + response);
                else System.out.println("No response");
            }
        }
    }
    static void showRatings(int foodID, Statement st, ResultSet rs) throws SQLException {
        rs = st.executeQuery("SELECT * FROM rating WHERE foodID=" + foodID);
        System.out.println("ratings:");
        while (rs.next()){
            int id=rs.getInt("rateID");
            double rate=rs.getDouble("rate");
            System.out.println(id + ".\t" + rate);
        }
    }
    static void showOpenOrders(int resID, Statement st, ResultSet rs) throws SQLException {
        st.executeUpdate("CREATE TABLE temp (SELECT `database`.order.orderID,`database`.order.status," +
                "                          `database`.order.customerID," +
                "                          orderdetails.foodID,orderdetails.count FROM orderdetails LEFT JOIN" +
                "                `database`.order ON orderdetails.orderID= `database`.order.orderID)");
        rs=st.executeQuery("SELECT `database`.temp.orderID, food.name, `database`.temp.count, " +
                "`database`.temp.status FROM temp LEFT JOIN food ON food.foodID=temp.foodID" +
                " WHERE resID=" + resID + " AND `database`.temp.status=\'preparing\' OR `database`.temp.status=\'delivering\'");
        while (rs.next()){
            int id=rs.getInt("orderID");
            String name=rs.getString("name");
            int count=rs.getInt("count");
            String status=rs.getString("status");
            System.out.println(id + "." + name + "\t" + count + "\t" + status);
        }
        st.executeUpdate("DROP TABLE temp");
    }
    static void showOrderHistory(int resID, Statement st, ResultSet rs) throws SQLException {
        st.executeUpdate("CREATE TABLE temp (SELECT `database`.order.orderID,`database`.order.status," +
                "                          `database`.order.customerID," +
                "                          orderdetails.foodID,orderdetails.count FROM orderdetails LEFT JOIN" +
                "                `database`.order ON orderdetails.orderID= `database`.order.orderID);");
        rs=st.executeQuery("SELECT temp.orderID, food.name, temp.count, temp.status FROM temp " +
                "LEFT JOIN food ON food.foodID=temp.foodID" +
                " WHERE resID=" + resID);
        while (rs.next()){
            int id=rs.getInt("orderID");
            String name=rs.getString("name");
            int count=rs.getInt("count");
            String status=rs.getString("status");
            System.out.println(id + "." + name + "\t" + count + "\t" + status);
        }
        st.executeUpdate("DROP TABLE temp");
    }
    static void showResComments(int resID, Statement st, ResultSet rs) throws SQLException {
        rs=st.executeQuery("SELECT * FROM rescomment WHERE resID=" + resID);
        System.out.println("Comments:");
        while (rs.next()){
            int id=rs.getInt("resCommentID");
            String comment= rs.getNString("resComment");
            System.out.println(id + "." + comment);
        }
    }
    static void showResRatings(int resID, Statement st, ResultSet rs) throws SQLException {
        rs=st.executeQuery("SELECT * FROM resrating WHERE resID=" + resID);
        System.out.println("rates:");
        while (rs.next()){
            int id=rs.getInt("resRateID");
            double rate=rs.getDouble("rate");
            System.out.println(id + "." + rate);
        }
    }
    static void showCart(int cusID, Statement st, ResultSet rs) throws SQLException{
        st.executeUpdate("CREATE TABLE temp (SELECT cartdetails.cartID, cartdetails.foodID, " +
                "cartdetails.count, cart.customerID, cart.totalPrice FROM cartdetails LEFT JOIN " +
                "cart ON cart.cartID=cartdetails.cartID)");
        rs=st.executeQuery("SELECT food.name, temp.count, food.price, food.resID, food.discount FROM temp LEFT JOIN " +
                        "food ON food.foodID=temp.foodID WHERE customerID=" + cusID);
        while (rs.next()){
            String name=rs.getString("name");
            int count=rs.getInt("count");
            String price=rs.getString("price");
            int dis=rs.getInt("discount");
            System.out.println(name + "\t" + count + "\t" + price + "\t" + dis + "%");
        }
        rs=st.executeQuery("SELECT * FROM cart WHERE cartID=" + cusID);
        double tot=0;
        while (rs.next()){
            tot=rs.getDouble("totalPrice");
        }
        System.out.println("Total Price : " + tot);
        st.executeUpdate("DROP TABLE temp");
    }
    static void addToCart(int foodID, int count, int cusID, Statement st, ResultSet rs) throws SQLException {
        st.executeUpdate("INSERT INTO cartdetails(cartID,foodID,cartdetails.count) " +
                "VALUES (" + cusID + "," + foodID + "," + count + ")");
        double price = 0,total = 0;
        int discount=0;
        rs= st.executeQuery("SELECT * FROM cart WHERE cartID=" + cusID);
        while(rs.next()){
            total= rs.getDouble("totalPrice");
        }
        rs= st.executeQuery("SELECT * FROM food WHERE foodID=" + foodID);
        while(rs.next()){
            price= rs.getDouble("price")*count;
            discount=rs.getInt("discount");
        }
        total=total+price*(100-discount)/100;
        st.executeUpdate("UPDATE cart SET totalPrice=" + total);
    }
    static void showOrderHistoryCus(int cusID, Statement st, ResultSet rs) throws SQLException {
        st.executeUpdate("CREATE TABLE temp (SELECT `database`.order.orderID,`database`.order.status," +
                "                          `database`.order.customerID," +
                "                          orderdetails.foodID,orderdetails.count FROM orderdetails LEFT JOIN" +
                "                `database`.order ON orderdetails.orderID= `database`.order.orderID);");
        rs= st.executeQuery("SELECT temp.orderID, food.name, temp.count, temp.status, food.resID " +
                        "FROM temp LEFT JOIN food ON food.foodID=temp.foodID" +
                        " WHERE customerID=" + cusID);
        while (rs.next()){
            int id=rs.getInt("orderID");
            String name=rs.getString("name");
            int count=rs.getInt("count");
            int resid=rs.getInt("resID");
            String status=rs.getString("status");
            System.out.println(id + "." + name + "\t" + count + "\t" + resid + "\t" + status);
        }
        st.executeUpdate("DROP TABLE temp");
    }
    static void showTime(int orderID, Statement st, ResultSet rs){

    }
    static void addResponse(String newResponse, int comID, Statement st, ResultSet rs) throws SQLException {
        st.executeUpdate("UPDATE comment SET response=\"" + newResponse + "\" WHERE commentID=" + comID);
        System.out.println("Response set.");
    }
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
            int manID = 0, resID = 0, cusID = 0, foodID = 0, orderID = 0;
            while (phase == 0) {
                System.out.println("login/register");
                command = input.nextLine();
                switch (command) {
                    case "add manager":
                        user = input.next();
                        pass = input.next();
                        System.out.println("Welcome Manager." + user + "!");
                        st.executeUpdate("INSERT INTO manager(userName,password) VALUES " +
                                "(\"" + user + "\",\"" + pass + "\")");
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
                        rs = st.executeQuery("SELECT * FROM customer WHERE userName=" + "\"" + user + "\" AND password=" + "\"" + pass + "\"");
                        while (rs.next()) {
                            cusID = rs.getInt("customerID");
                        }
                        st.executeUpdate("INSERT INTO cart(cartID,customerID,totalPrice) VALUES " +
                                "(" + cusID + "," + cusID + ",0)");
                        phase = 3;
                        break;//customer's page
                    case "add delivery":
                        user = input.next();
                        pass = input.next();
                        System.out.println("Welcome delivery." + user + "!");
                        st.executeUpdate("INSERT INTO delivery(userName,password) VALUES(\""
                                + user + "\",\"" + pass + "\")");
                        phase = 6; //delivery's page
                    case "login manager":
                        System.out.println("Enter username and password:");
                        user=input.next();
                        pass=input.next();
                        rs=st.executeQuery("SELECT * FROM manager");
                        String manUse="" , manPass="";
                        boolean findMan=false;
                        while (rs.next()){
                            manUse=rs.getString("userName");
                            manPass=rs.getString("password");
                            if(manUse.equals(user) && manPass.equals(pass)){
                                findMan=true;
                                break;
                            }
                        }
                        if(findMan) {
                            phase=1;
                            break;
                        }
                        else{
                            System.out.println("There is no account with this username or password!");
                            break;
                        }
                    case "login customer":
                        System.out.println("Enter username and password:");
                        user=input.next();
                        pass=input.next();
                        rs=st.executeQuery("SELECT * FROM customer");
                        String cusUse="" , cusPass="";
                        boolean findCus=false;
                        while (rs.next()){
                            cusUse=rs.getString("userName");
                            cusPass=rs.getString("password");
                            if(cusUse.equals(user) && cusPass.equals(pass)){
                                findCus=true;
                                break;
                            }
                        }
                        if(findCus) phase=3;
                        else{
                            System.out.println("There is no account with this username or password!");
                        }
                }
            }   //login & register
            while (phase == 1) {
                rs = st.executeQuery("SELECT * FROM manager WHERE userName=" + "\"" + user + "\" AND password=" + "\"" + pass + "\"");
                while (rs.next()) {
                    manID = rs.getInt("managerID");
                }
                showRestaurants(manID, st, rs);
                command = input.nextLine();
                if(command.equals("return")) phase=0;
                if (command.equals("add restaurant")) {
                    System.out.println("Please enter restaurant name, x, y, food type!");
                    String name = input.nextLine();
                    int x = input.nextInt();
                    int y = input.nextInt();
                    String foodType = input.next();
                    addRestaurant(name, x, y, foodType, manID, st);
                    rs = st.executeQuery("SELECT * FROM restaurant WHERE managerID=" + manID + " AND name=" + "\"" + name + "\"");
                    while (rs.next()) {
                        resID = rs.getInt("resID");
                    }
                    phase = 2; //manager's restaurant page
                }
                if (command.equals("select restaurant")) {
                    System.out.println("Please enter your restaurant ID:");
                    int id=input.nextInt();
                    resID=id;
                    phase = 2; //manager's restaurant page
                }
            }   //manager's page
            while (phase == 2) {
                String resname="";
                rs=st.executeQuery("SELECT * FROM restaurant WHERE resID=" + resID);
                while (rs.next()){
                    resname=rs.getString("name");
                }
                System.out.println(resname + ":");
                command = input.nextLine();
                if (command.equals("return")) phase=1;
                if (command.equals("show menu")) {
                    showMenu(resID, st, rs);
                    command = input.nextLine();
                    if(command.equals("return")) phase=2;
                    if (command.equals("add food")) {
                        System.out.println("Please add food name, price!");
                        while (true) {
                            String name = input.next();
                            if(name.equals("end")){
                                break;
                            }
                            double price = input.nextDouble();
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
                            st.executeUpdate("UPDATE food SET name=\"" + New + "\" WHERE foodID=" + id);
                        } else {
                            double New = input.nextDouble();
                            st.executeUpdate("UPDATE food SET price=" + New + " WHERE foodID=" + id);
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
                    if (command.equals("discount food")){
                        System.out.println("Enter the f***ing food ID you want to discount:");
                        int id=input.nextInt();
                        System.out.println("How much discount do you want you douche");
                        int dis=input.nextInt();
                        st.executeUpdate("UPDATE food SET discount=" + dis + " WHERE foodID=" + id);
                        System.out.println("Discount complete!");
                    }
                    if (command.equals("activate food")) {
                        System.out.println("Enter the food ID you want to active!");
                        int id = input.nextInt();
                        st.executeUpdate("UPDATE food SET active=1 WHERE foodID=" + id);
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
                                st.executeUpdate("UPDATE food SET active=0 WHERE foodID=" + id);
                                System.out.println("Food item deactivated:((((");
                            }
                        } else {
                            System.out.println("Canceled deactivating");
                        }
                    }
                    if (command.equals("select food")) {
                        String com="";
                        System.out.println("Enter the food ID you want to select");
                        int id=input.nextInt();
                        foodID=id;
                        phase=6;
                    }
                }
                if (command.equals("show location")) {
                    rs=st.executeQuery("SELECT * FROM restaurant WHERE resID=" + resID);
                    int x=0,y=0;
                    while (rs.next()){
                        x=rs.getInt("x");
                        y=rs.getInt("y");
                    }
                    System.out.println("x: " + x + "\t" + "y: " + y);
                }
                if (command.equals("show food type")) {
                    rs=st.executeQuery("SELECT * FROM restaurant WHERE resID=" + resID);
                    String FT="";
                    while (rs.next()){
                        FT=rs.getString("foodType");
                    }
                    System.out.println("Food Type: " + FT);
                }
                if (command.equals("edit food type")) {
                    System.out.println("Enter the new food type:");
                    String newFT=input.nextLine();
                    st.executeUpdate("UPDATE restaurant SET foodType=\"" + newFT + "\" WHERE resID=" + resID);
                    System.out.println("Food type changed to " + newFT + ".");
                }
                if (command.equals("show open orders")) {
                    String com="";
                    showOpenOrders(resID,st,rs);
                    com=input.nextLine();
                    if(com.equals("return")) phase=2;
                    if (com.equals("edit order")) {
                        System.out.println("Enter the order ID:");
                        int ordID=input.nextInt();
                        System.out.println("Enter the new status:");
                        String newStatus=input.next();
                        st.executeUpdate("UPDATE database.order SET status=\"" + newStatus + "\" WHERE orderID=" + ordID);
                        System.out.println("Status changed.");
                    }
                }
                if (command.equals("show order history")) {
                    showOrderHistory(resID,st,rs);
                }
            }   //manager's restaurant page
            while (phase == 3) {
                rs = st.executeQuery("SELECT * FROM customer WHERE userName=" + "\"" + user + "\" AND password=" + "\"" + pass + "\"");
                while (rs.next()) {
                    cusID = rs.getInt("customerID");
                }
                rs= st.executeQuery("SELECT * FROM restaurant");
                while (rs.next()){
                    int id=rs.getInt("resID");
                    String name=rs.getString("name");
                    System.out.println(id + "." + name);
                }
                command=input.nextLine();
                if (command.equals("return")) phase=0;
                if (command.equals("search")){
                    System.out.println("Enter the restaurant name:");
                    String com=input.nextLine();
                    rs= st.executeQuery("SELECT * FROM restaurant WHERE name=\"" + com + "\"");
                    while (rs.next()){
                        int id=rs.getInt("resID");
                        String name=rs.getString("name");
                        System.out.println(id + "." + name);
                    }
                    System.out.println("Enter the restaurant ID:");
                    resID=input.nextInt();
                    phase=4; //customer's restaurant page
                }
                else if (command.equals("select")){
                    System.out.println("Enter the restaurant ID:");
                    resID=input.nextInt();
                    phase=4; //customer's restaurant page
                }
                if (command.equals("change balance")){
                    System.out.println("Enter new balance:");
                    double newPrice= input.nextDouble();
                    st.executeUpdate("UPDATE customer SET balance=" + newPrice + " WHERE customerID=" + cusID);
                }
                if (command.equals("show cart")){
                    showCart(cusID,st,rs);
                    System.out.println("confirm order?");
                    String confirmation=input.next();
                    if(confirmation.equals("yes")){
                        st.executeUpdate("INSERT INTO database.order(status,customerID) " +
                                "VALUES (\"preparing\"," + cusID + ")");
                        rs=st.executeQuery("SELECT * FROM database.order WHERE customerID="+cusID);
                        while (rs.next()) {
                            orderID=rs.getInt("orderID");
                        }
                        int[] foodid=new int[100];
                        int[] count=new int[100];
                        int i=0;
                        rs=st.executeQuery("SELECT * FROM cartdetails WHERE cartID="+cusID);
                        while (rs.next()){
                            foodid[i]=rs.getInt("foodID");
                            count[i]=rs.getInt("count");
                            i++;
                        }
                        for (int j=0;j<i;j++) {
                            st.executeUpdate("INSERT INTO orderdetails(orderId,foodID,orderdetails.count)" +
                                    " VALUES(" + orderID + "," + foodid[j] + "," + count[j] + ")");
                        }
                        i=0;
                        st.executeUpdate("DELETE FROM cartdetails WHERE cartID="+cusID);
                        rs=st.executeQuery("SELECT * FROM customer WHERE customerID=" + cusID);
                        double balance=0,tot=0;
                        while (rs.next()){
                            balance=rs.getDouble("balance");
                        }
                        rs= st.executeQuery("SELECT * FROM cart WHERE cartID=" + cusID);
                        while (rs.next()){
                            tot=rs.getDouble("totalPrice");
                        }
                        balance=balance-tot;
                        System.out.println("your order is now preparing.");
                        System.out.println("Your new balance is: " + balance);
                        st.executeUpdate("UPDATE customer SET balance=" + balance + " WHERE customerID=" + cusID);
                        st.executeUpdate("UPDATE cart SET totalPrice=0 WHERE customerID=" + cusID);
                    }
                    else{
                        phase=3;
                    }
                }
                if (command.equals("show order history")){
                    showOrderHistoryCus(cusID,st,rs);
                }
                if (command.equals("show balance")){
                    rs=st.executeQuery("SELECT * from customer WHERE customerID=" + cusID);
                    double balance=rs.getDouble("balance");
                    System.out.println("Your balance is: " + balance);
                }
                if (command.equals("show estimated time")){
                    showTime(orderID,st,rs);
                }
                if (command.equals("show order status")){
                    rs=st.executeQuery("SELECT * FROM database.order WHERE customerID= " + cusID);
                    while (rs.next()){
                        String stat=rs.getString("status");
                        System.out.println("Your order is " + stat);
                    }
                }
            }   //customer's page
            while (phase == 4) {
                showMenu(resID,st,rs);
                System.out.println("Select food or search:");
                command=input.nextLine();
                if(command.equals("return")) phase=3;
                if(command.equals("search")){
                    System.out.println("Enter the food name:");
                    String com=input.nextLine();
                    rs= st.executeQuery("SELECT * FROM food WHERE name=\"" + com + "\" AND resID=" + resID + " AND active=1");
                    while (rs.next()){
                        int id=rs.getInt("foodID");
                        String name=rs.getString("name");
                        double price=rs.getDouble("price");
                        System.out.println(id + "." + name + "\t" + price);
                    }
                    System.out.println("Enter the food ID:");
                    foodID=input.nextInt();
                    phase=5; //food page
                }
                else if(command.equals("select")){
                    System.out.println("Enter the food ID:");
                    foodID=input.nextInt();
                    phase=5; //food page
                }
                if(command.equals("display comments")){
                    showResComments(resID,st,rs);
                }
                if(command.equals("add comment")){
                    rs=st.executeQuery("SELECT * FROM rescomment WHERE resID="+resID);
                    boolean found=false;
                    while(rs.next()){
                        int customerid=rs.getInt("customerID");
                        if(customerid==cusID){
                            found=true;
                            break;
                        }
                    }
                    if(found){
                        System.out.println("YOU HAVE ALREADY COMMENTED ON THIS RESTAURANT!!!!!!!");
                        System.out.println("GET OUT OF HERE NOW. GO GET A LIFE!!!!!!!!!!!");
                    }
                    else{
                        System.out.println("INSERT COMMENT YOU ....");
                        String comm=input.nextLine();
                        st.executeUpdate("INSERT INTO rescomment(customerID, resID, resComment) " +
                                "VALUES(" + cusID + "," + resID + ",\"" + comm + "\")");
                    }
                }
                if(command.equals("edit comment")){
                    System.out.println("Enter the comment ID you wish to edit");
                    int comID=input.nextInt();
                    boolean foundid=false;
                    rs=st.executeQuery("SELECT * FROM rescomment WHERE resID="+resID);
                    while(rs.next()){
                        int commentID=rs.getInt("commentID");
                        int customerID=rs.getInt("customerID");
                        if(commentID==comID && customerID==cusID){
                            foundid=true;
                            break;
                        }
                    }
                    if(foundid){
                        System.out.println("Enter the new comment and it shall be commented!");
                        String newcomm=input.nextLine();
                        st.executeUpdate("UPDATE rescomment SET resComment=\"" + newcomm +
                                "\" WHERE commentID="+comID);
                    }
                    else {
                        System.out.println("There are no comments with this id");
                    }
                }
                if(command.equals("display ratings")){
                    showResRatings(resID,st,rs);
                }
                if(command.equals("add rating")){
                    rs=st.executeQuery("SELECT * FROM resrating WHERE resID="+resID);
                    boolean found=false;
                    while(rs.next()){
                        int customerid=rs.getInt("customerID");
                        if(customerid==cusID){
                            found=true;
                            break;
                        }
                    }
                    if(found){
                        System.out.println("YOU HAVE ALREADY RATED THIS RESTAURANT!!!!!!!");
                        System.out.println("GET OUT OF HERE NOW. GO GET A LIFE!!!!!!!!!!!");
                    }
                    else{
                        System.out.println("RATE THE RESTAURANT YOU ....");
                        double rate=input.nextDouble();
                        st.executeUpdate("INSERT INTO resrating(customerID, resID, rate) " +
                                "VALUES(" + cusID + "," + resID + "," + rate + ")");
                    }
                }
                if(command.equals("edit rating")){
                    System.out.println("Enter the rate ID you wish to edit");
                    double ratID=input.nextDouble();
                    boolean foundid=false;
                    rs=st.executeQuery("SELECT * FROM resrating WHERE resID="+resID);
                    while(rs.next()){
                        int rateID=rs.getInt("rateID");
                        int customerID=rs.getInt("customerID");
                        if(rateID==ratID && customerID==cusID){
                            foundid=true;
                            break;
                        }
                    }
                    if(foundid){
                        System.out.println("Enter the new rating and it shall be rated!");
                        double newRate=input.nextDouble();
                        st.executeUpdate("UPDATE resrating SET rate=" + newRate +
                                " WHERE rateID="+ratID);
                    }
                    else {
                        System.out.println("There are no ratings with this id");
                    }
                }
            }   //customer's restaurant page
            while (phase == 5) {
                rs=st.executeQuery("SELECT * FROM food WHERE foodID=" + foodID);
                while (rs.next()){
                    String name=rs.getString("name");
                    System.out.println(name + ":");
                }
                command=input.nextLine();
                if(command.equals("return")) phase=4;
                if(command.equals("add to cart")){
                    System.out.println("How many of the selected food?");
                    int foocou=input.nextInt();
                    addToCart(foodID,foocou,cusID,st,rs);
                    System.out.println("Food item added to your cart successfully.");
                }
                if(command.equals("display comments")){
                    showComments(foodID,st,rs);
                }
                if(command.equals("add comment")){
                    rs=st.executeQuery("SELECT * FROM comment WHERE foodID="+foodID);
                    boolean found=false;
                    while(rs.next()){
                        int customerid=rs.getInt("customerID");
                        if(customerid==cusID){
                            found=true;
                            break;
                        }
                    }
                    if(found){
                        System.out.println("YOU HAVE ALREADY COMMENTED ON THIS FOOD!!!!!!!");
                        System.out.println("GET OUT OF HERE NOW. GO GET A LIFE!!!!!!!!!!!");
                    }
                    else{
                        System.out.println("INSERT COMMENT YOU ....");
                        String comm=input.nextLine();
                        st.executeUpdate("INSERT INTO comment(response, customerID, comment, foodID) " +
                                "VALUES(\"\"," + cusID + ",\"" + comm + "\","+ foodID + ")");
                    }
                }
                if(command.equals("edit comment")){
                    System.out.println("Enter the comment ID you wish to edit");
                    int comID=input.nextInt();
                    boolean foundid=false;
                    rs=st.executeQuery("SELECT * FROM comment WHERE foodID="+foodID);
                    while(rs.next()){
                        int commentID=rs.getInt("commentID");
                        int customerID=rs.getInt("customerID");
                        if(commentID==comID && customerID==cusID){
                            foundid=true;
                            break;
                        }
                    }
                    if(foundid){
                        System.out.println("Enter the new comment and it shall be commented!");
                        String newcomm="";
                        String comm=input.next();
                        while(!comm.equals("!")){
                            newcomm+=comm + " ";
                            comm=input.next();
                        }
                        st.executeUpdate("UPDATE comment SET comment=\"" + newcomm +
                                "\" WHERE commentID=" + comID);
                    }
                    else {
                        System.out.println("There are no comments with this id");
                    }
                }
                if(command.equals("display ratings")){
                    showRatings(foodID,st,rs);
                }
                if(command.equals("add rating")){
                    rs=st.executeQuery("SELECT * FROM rating WHERE foodID=" + foodID);
                    boolean found=false;
                    while(rs.next()){
                        int customerid=rs.getInt("customerID");
                        if(customerid==cusID){
                            found=true;
                            break;
                        }
                    }
                    if(found){
                        System.out.println("YOU HAVE ALREADY RATED THIS FOOD!!!!!!!");
                        System.out.println("GET OUT OF HERE NOW. GO GET A LIFE!!!!!!!!!!!");
                    }
                    else{
                        System.out.println("RATE THE FOOD YOU ....");
                        double rate=input.nextDouble();
                        st.executeUpdate("INSERT INTO rating(customerID,rate,foodID) " +
                                "VALUES(" + cusID + "," + rate + "," + foodID + ")");
                    }
                }
                if(command.equals("edit rating")){
                    System.out.println("Enter the rate ID you wish to edit");
                    double ratID=input.nextDouble();
                    boolean foundid=false;
                    rs=st.executeQuery("SELECT * FROM rating WHERE foodID="+foodID);
                    while(rs.next()){
                        int rateID=rs.getInt("rateID");
                        int customerID=rs.getInt("customerID");
                        if(rateID==ratID && customerID==cusID){
                            foundid=true;
                            break;
                        }
                    }
                    if(foundid){
                        System.out.println("Enter the new rating and it shall be rated!");
                        double newRate=input.nextDouble();
                        st.executeUpdate("UPDATE rating SET rate=" + newRate +
                                " WHERE rateID="+ratID);
                        System.out.println("New rating= " + newRate + "!");
                    }
                    else {
                        System.out.println("There are no ratings with this id");
                    }
                }
            }   //customer's food page
            while (phase == 6) {
                rs=st.executeQuery("SELECT * FROM food WHERE foodID=" + foodID);
                while (rs.next()){
                    String name=rs.getString("name");
                    System.out.println(name + ":");
                }
                command=input.nextLine();
                if (command.equals("show comments")) {
                    showComments(foodID,st,rs);
                }
                if (command.equals("add response")) {
                    System.out.println("Enter the comment ID you want to respond to");
                    int comid=input.nextInt();
                    String newResponse="";
                    String Response=input.next();
                    while(!Response.equals("!")){
                        newResponse+=Response + " ";
                        Response=input.next();
                    }
                    System.out.println(newResponse);
                    addResponse(newResponse,comid,st,rs);
                }
                if (command.equals("edit response")) {
                    System.out.println("Enter the comment ID you want to edit the response");
                    int comid=input.nextInt();
                    rs = st.executeQuery("SELECT * FROM comment WHERE commentID=" + comid);
                    String oldResponse="";
                    while (rs.next()) {
                        oldResponse=rs.getString("response");
                    }
                    if(oldResponse.equals("")){
                        System.out.println("YOU CAN NOT EDIT A RESPONSE THAT DOES NOT EXIST!!!!!");
                        System.out.println("TRY ADDING ONE YOU PIECE OF SHIT!!!!!");
                    }
                    else {
                        String newResponse="";
                        String Response=input.next();
                        while(!Response.equals("!")){
                            newResponse+=Response + " ";
                            Response=input.next();
                        }
                        st.executeUpdate("UPDATE comment SET response=\"" + newResponse + "\" WHERE commentID=" + comid);
                        System.out.println("Response set.");
                    }
                }
                if (command.equals("show ratings")) {
                    int foodid=input.nextInt();
                    rs = st.executeQuery("SELECT * FROM rating WHERE foodID=" + foodid);
                    while (rs.next()){
                        double rate=rs.getDouble("rate");
                        System.out.print(rate + "\t");
                    }
                    System.out.println();
                }
                if (command.equals("return")) phase=2;
            }   //manager's food page
        }
    }
}


