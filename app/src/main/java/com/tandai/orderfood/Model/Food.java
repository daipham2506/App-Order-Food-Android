package com.tandai.orderfood.Model;

public class Food {
    private String tenMon;
    private String tenQuan;
    private String linkAnh;
    private String IDQuan;
    private long giaMon;
    private int tinhTrang;

    public Food() {
    }

    public Food(String tenMon, String tenQuan, String linkAnh, String IDQuan, long giaMon, int tinhTrang) {

        this.tenMon = tenMon;
        this.tenQuan = tenQuan;
        this.linkAnh = linkAnh;
        this.IDQuan = IDQuan;
        this.giaMon = giaMon;
        this.tinhTrang = tinhTrang;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getTenQuan() {
        return tenQuan;
    }

    public void setTenQuan(String tenQuan) {
        this.tenQuan = tenQuan;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public String getIDQuan() {
        return IDQuan;
    }

    public void setIDQuan(String IDQuan) {
        this.IDQuan = IDQuan;
    }

    public long getGiaMon() {
        return giaMon;
    }

    public void setGiaMon(long giaMon) {
        this.giaMon = giaMon;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
}
