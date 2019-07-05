package com.tandai.orderfood.Model;

public class Rating {
    private String name;
    private String restaurentID;
    private String foodID;
    private String rateValue;
    private String comment;
    private String dateTime;

    public Rating() {
    }

    public Rating(String name,String restaurentID, String foodID, String rateValue, String comment,String dateTime) {
        this.name = name;
        this.restaurentID = restaurentID;
        this.foodID = foodID;
        this.rateValue = rateValue;
        this.comment = comment;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
