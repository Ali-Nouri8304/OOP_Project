import java.util.ArrayList;

public class Menu {
    ArrayList<Food> foods;
    void editFood(int id,String element,String newElement){
        int foodPlace= findFood(id).id;
        if(element.equals("name")){
            foods.get(foodPlace).name=newElement;
        }
        else if(element.equals("price")){
            foods.get(foodPlace).price=(int) (Long.parseLong(newElement));
        }
    }
    void addFood(String name,int price){
        Food newFood=new Food(name,this.foods.size(),price,0);
        this.foods.add(newFood);
    }
    void deleteFood(int id){
        foods.remove(id);
        for(int i=id;i<this.foods.size();i++){
            foods.get(i).id--;
        }
    }
    void deactivateFood(int id){
        if(foods.get(id).ongoingOrder==0)
            foods.get(id).active=false;
        else
            System.out.println("There is an ongoing order containing this food item!");
    }
    void discountFood(int id,int discount){

    }
    void activateFood(int id){
        foods.get(id).active=true;
    }
    Food findFood(int id){
        int a=-1;
        for(int i=0;i<foods.size();i++){
            if(foods.get(i).id==id) {
                a = i;
                break;
            }
        }
        return foods.get(a);
    }
}
