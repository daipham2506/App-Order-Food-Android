package com.tandai.orderfood.Model;

public class MonAn {
    private String tenQuan;
    private String idQuan;
    private String tenMon;
    private String linkAnh;
    private long giaMon;
    private int tinhTrang;

    public MonAn() {
    }

    public MonAn(String tenQuan, String idQuan, String tenMon, String linkAnh, long giaMon,int tinhTrang) {
        this.tenQuan = tenQuan;
        this.idQuan = idQuan;
        this.tenMon = tenMon;
        this.linkAnh = linkAnh;
        this.giaMon = giaMon;
        this.tinhTrang = tinhTrang;
    }

    public String getTenQuan() {
        return tenQuan;
    }

    public void setTenQuan(String tenQuan) {
        this.tenQuan = tenQuan;
    }

    public String getIdQuan() {
        return idQuan;
    }

    public void setIdQuan(String idQuan) {
        this.idQuan = idQuan;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public long getGiaMon() {
        return giaMon;
    }

    public void setGiaMon(long giaMon) {
        this.giaMon = giaMon;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
}
