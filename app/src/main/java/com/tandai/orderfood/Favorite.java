package com.tandai.orderfood;

public class Favorite {
    private String foodID;
    private String userID;
    private String restaurentID;
    private int check;

    public Favorite() {
    }

    public Favorite(String foodID, String userID, String restaurentID, int check) {
        this.foodID = foodID;
        this.userID = userID;
        this.restaurentID = restaurentID;
        this.check = check;
    }


    public String getRestaurentID() {
        return restaurentID;
    }

    public void setRestaurentID(String restaurentID) {
        this.restaurentID = restaurentID;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
