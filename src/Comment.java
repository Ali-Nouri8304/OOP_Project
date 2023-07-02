public class Comment {
    int comID;
    int foodID;
    String comment;
    String response;
    public void addComment(String com){
        this.comment=com;
    }
    public void addResponse(String res){
        this.response=res;
    }
    public void editComment(String newCom){
        this.comment=newCom;
    }
    public void editResponse(String newRes){
        this.response=newRes;
    }
}
