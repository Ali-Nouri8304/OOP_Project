import java.util.ArrayList;

public class Manager {
    private String userName;
    private String password;
    ArrayList<Restaurant> managerRestaurant;

    public void setPassword(String password) { this.password = password;}
    public void setUserName(String userName) { this.userName = userName;}

    public String getPassword() { return password;}
    public String getUserName() { return userName;}

    Manager(String userName, String password, ArrayList<Restaurant> restaurants){
        this.setPassword(password);
        this.setUserName(userName);
        this.managerRestaurant=new ArrayList<>(restaurants);
    }
    Restaurant findRes(int id){
        int a=-1;
        for(int i=0;i<managerRestaurant.size();i++){
            if(managerRestaurant.get(i).ID==id) {
                a = i;
                break;
            }
        }
        return managerRestaurant.get(a);
    }
}

