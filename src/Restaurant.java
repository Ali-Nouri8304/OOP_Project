import java.util.ArrayList;

public class Restaurant {
    int ID;
    int x;
    int y;
    String foodType;
    Menu menu = new Menu();
    ArrayList<Order> orders;
    void showLocation(){
        System.out.println(this.x +" "+this.y);
    }
    void editLocation(int x, int y){
        this.x=x;
        this.y=y;
    }
    void showFoodType(){
        System.out.println(this.foodType);
    }
    void editFoodType(String newFoodType){
        this.foodType=newFoodType;
    }


}
