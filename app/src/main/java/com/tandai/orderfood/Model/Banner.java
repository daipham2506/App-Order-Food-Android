package com.tandai.orderfood.Model;

public class Banner {
    private String id,idQuan,image;

    public Banner() {
    }

    public Banner(String id, String idQuan, String image) {
        this.id = id;
        this.idQuan = idQuan;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdQuan() {
        return idQuan;
    }

    public void setIdQuan(String idQuan) {
        this.idQuan = idQuan;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
