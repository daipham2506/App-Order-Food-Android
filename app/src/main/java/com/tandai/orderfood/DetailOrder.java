package com.tandai.orderfood;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import info.hoang8f.widget.FButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailOrder extends AppCompatActivity {
    String foodID = "";
    String CustomerID = "";
    RadioGroup radioGroup;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView tenmon, gia, tenKh, sdt, diachi, soluong, ngaydathang, tongtien;
    ImageView hinh;
    FButton xacnhan;
    RadioButton dagiao, danggiao, hethang;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    Order order;
    long quantity;
    long gia1mon;

    DatabaseReference database;


    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Note  add this code before setcontentView
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Rubik.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.layout_detail_order);
        AnhXa();


        //Nhận THông tin Order từ Intent gửi đến
        Intent intent = getIntent();
        if (intent != null) {
            foodID = intent.getStringExtra("FoodID");
            CustomerID = intent.getStringExtra("CustomerID");
        }
        if (!foodID.isEmpty() && foodID != null && !CustomerID.isEmpty() && CustomerID != null) {
            getDataOrder(CustomerID, foodID);
        }


        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dagiao.isChecked()) {
                    Toast.makeText(DetailOrder.this, "Đã xác nhận đơn hàng", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailOrder.this, QuanAnActivity.class));
                    //delOrderAfterConfirm(CustomerID,foodID);
                }
                else if (danggiao.isChecked()) {

                    Toast.makeText(DetailOrder.this, "Đã xác nhận đơn hàng", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailOrder.this, QuanAnActivity.class));
                    //delOrderAfterConfirm(CustomerID,foodID);
                }
                else if (hethang.isChecked()) {

                    Toast.makeText(DetailOrder.this, "Đã xác nhận đơn hàng", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(DetailOrder.this, QuanAnActivity.class));
                    //delOrderAfterConfirm(CustomerID,foodID);
                } else {
                    Toast.makeText(DetailOrder.this, "Vui lòng chọn tình trạng giao hàng", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }


    private void AnhXa() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupShip);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingOrder);
        tenmon = (TextView) findViewById(R.id.tenmon);
        gia = (TextView) findViewById(R.id.tien);
        tenKh = (TextView) findViewById(R.id.ten);
        sdt = (TextView) findViewById(R.id.sdt);
        diachi = (TextView) findViewById(R.id.diachi);
        soluong = (TextView) findViewById(R.id.soluong);
        ngaydathang = (TextView) findViewById(R.id.ngaydathang);
        tongtien = (TextView) findViewById(R.id.tongtien);
        hinh = (ImageView) findViewById(R.id.hinh);
        xacnhan = (FButton) findViewById(R.id.xacnhan);
        dagiao = (RadioButton) findViewById(R.id.dagiao);
        danggiao = (RadioButton) findViewById(R.id.danggiao);
        hethang = (RadioButton) findViewById(R.id.hethang);
    }

    private void getDataOrder(final String CustomerID, final String foodID) {

        mDatabase.child("Orders").child(userID).child(CustomerID).child(foodID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                // đối tượng order lấy dữ liệu từ database
                order = dataSnapshot.getValue(Order.class);
                //THiết lập ảnh
                Picasso.with(getBaseContext()).load(order.getLinkAnh()).into(hinh);
                quantity = order.getSoluong();
                gia1mon = order.getGiaMon();
                gia.setText(gia1mon + "đ");
                tenmon.setText(order.getTenMon());
                tenKh.setText("Tên: " + order.getTenkhachhang());
                sdt.setText("Sđt: " + order.getSdtKhachHang());
                diachi.setText("Địa chỉ giao hàng: " + order.getDiachigiaohang());
                ngaydathang.setText("Ngày đặt hàng: " + order.getDateTime());
                soluong.setText("Số lượng: " + order.getSoluong());
                tongtien.setText("Tổng tiền: " + gia1mon * quantity + "đ");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }





}
