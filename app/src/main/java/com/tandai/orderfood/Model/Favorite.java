package com.tandai.orderfood.Model;

public class Favorite {
    private String foodID;
    private String userID;
    private String restaurentID;
    private long price;
    private String image;
    private int check;

    public Favorite() {
    }

    public Favorite(String foodID, String userID, String restaurentID, long price, String image, int check) {
        this.foodID = foodID;
        this.userID = userID;
        this.restaurentID = restaurentID;
        this.price = price;
        this.image = image;
        this.check = check;
    }


    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
