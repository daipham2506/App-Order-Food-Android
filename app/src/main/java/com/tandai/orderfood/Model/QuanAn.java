package com.tandai.orderfood.Model;

public class QuanAn {
    private String tenQuan;
    private String diachi;
    private String sdt;

    public QuanAn(String tenQuan, String diachi, String sdt) {
        this.tenQuan = tenQuan;
        this.diachi = diachi;
        this.sdt = sdt;
    }
    public QuanAn(){}

    public String getTenQuan() {
        return tenQuan;
    }

    public void setTenQuan(String tenQuan) {
        this.tenQuan = tenQuan;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
