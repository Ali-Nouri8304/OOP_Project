import java.util.ArrayList;

public class Food {
    String name;
    int foodID;
    int resID;
    double price;
    int discount;
    ArrayList<Double> rate = new ArrayList<>();
    ArrayList<Comment> comments = new ArrayList<>();
    double rating=0.0;
    boolean active=true;
    int ongoingOrders=0;
    public Food(String name,int id,int price,int discount){
        this.name=name;
        this.price=price;
        this.foodID=id;
        this.discount=discount;
    }
    Comment findCom(int id){
        int a=-1;
        for(int i=0;i<comments.size();i++){
            if(comments.get(i).comID==id) {
                a = i;
                break;
            }
        }
        return comments.get(a);
    }
}
