import java.util.ArrayList;

public class Manager {
    int managerID;
    private String userName;
    private String password;
    ArrayList<Restaurant> managerRestaurant;

    public void setPassword(String password) { this.password = password;}
    public void setUserName(String userName) { this.userName = userName;}

    public String getPassword() { return password;}
    public String getUserName() { return userName;}

    Manager(int id, String userName, String password){
        this.managerID=id;
        this.setPassword(password);
        this.setUserName(userName);
    }
    Restaurant findRes(int id){
        int a=-1;
        for(int i=0;i<managerRestaurant.size();i++){
            if(managerRestaurant.get(i).resID==id) {
                a = i;
                break;
            }
        }
        return managerRestaurant.get(a);
    }
}

