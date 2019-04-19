package com.tandai.orderfood;

public class Order {

    private String diachigiaohang;
    private String sdtKhachHang;
    private String UserID;
    private String tenkhachhang;
    private String tenQuan;
    private String QuanID;
    private String tenMon;
    private long giaMon;
    private long soluong;
    private String linkAnh;

    public Order() {
    }

    public Order(String diachigiaohang, String sdtKhachHang, String userID, String tenkhachhang, String tenQuan, String quanID, String tenMon, long giaMon, long soluong, String linkAnh) {
        this.diachigiaohang = diachigiaohang;
        this.sdtKhachHang = sdtKhachHang;
        UserID = userID;
        this.tenkhachhang = tenkhachhang;
        this.tenQuan = tenQuan;
        QuanID = quanID;
        this.tenMon = tenMon;
        this.giaMon = giaMon;
        this.soluong = soluong;
        this.linkAnh = linkAnh;
    }

    public String getDiachigiaohang() {
        return diachigiaohang;
    }

    public void setDiachigiaohang(String diachigiaohang) {
        this.diachigiaohang = diachigiaohang;
    }

    public String getSdtKhachHang() {
        return sdtKhachHang;
    }

    public void setSdtKhachHang(String sdtKhachHang) {
        this.sdtKhachHang = sdtKhachHang;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getTenkhachhang() {
        return tenkhachhang;
    }

    public void setTenkhachhang(String tenkhachhang) {
        this.tenkhachhang = tenkhachhang;
    }

    public String getTenQuan() {
        return tenQuan;
    }

    public void setTenQuan(String tenQuan) {
        this.tenQuan = tenQuan;
    }

    public String getQuanID() {
        return QuanID;
    }

    public void setQuanID(String quanID) {
        QuanID = quanID;
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

    public long getSoluong() {
        return soluong;
    }

    public void setSoluong(long soluong) {
        this.soluong = soluong;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }
}
