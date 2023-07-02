import java.util.ArrayList;

public class Order {
    boolean ongoing = false;
    int orderID;
    ArrayList<Food> foods = new ArrayList<>();
    ArrayList<Integer> foodCounts = new ArrayList<>();
    int x_destination;
    int y_destination;
    Customer customer;
    String Status;
    Order(int ID, ArrayList<Food> food, ArrayList<Integer> foodCount, int x, int y, Customer person){
        this.orderID=ID;
        this.foods=food;
        this.foodCounts=foodCount;
        this.x_destination=x;
        this.y_destination=y;
        this.customer=person;
        this.ongoing=true;
    }
}
