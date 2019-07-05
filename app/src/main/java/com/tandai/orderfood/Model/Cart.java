package com.tandai.orderfood.Model;

public class Cart {
    private String tenMon;
    private String tenQuan;
    private String IDQuan;
    private String linkAnh;
    private long giaMon;
    private long soluong;
    private  long tongTien;

    public Cart() {
    }

    public Cart(String tenMon, String tenQuan, String IDQuan, String linkAnh,long giaMon, long soluong, long tongTien) {
        this.tenMon = tenMon;
        this.tenQuan = tenQuan;
        this.IDQuan = IDQuan;
        this.linkAnh = linkAnh;
        this.giaMon = giaMon;
        this.soluong = soluong;
        this.tongTien = tongTien;
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

    public long getSoluong() {
        return soluong;
    }

    public void setSoluong(long soluong) {
        this.soluong = soluong;
    }

    public long getTongTien() {
        return tongTien;
    }

    public void setTongTien(long tongTien) {
        this.tongTien = tongTien;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }
}
