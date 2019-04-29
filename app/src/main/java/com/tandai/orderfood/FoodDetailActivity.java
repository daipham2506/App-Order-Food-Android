package com.tandai.orderfood;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class FoodDetailActivity extends AppCompatActivity {
    User quanAn;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String foodId = "";
    String RestaurentID = "";
    FloatingActionButton btnCart;
    ElegantNumberButton number;
    ImageView hinh;
    TextView tenmon,giamon,mota;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    MonAn food;
    long quantity = 1 , price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_food_detail);

        //anh xa
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);
        number = (ElegantNumberButton) findViewById(R.id.number_button);
        hinh = (ImageView) findViewById(R.id.image_food);
        tenmon= (TextView) findViewById(R.id.food_name);
        giamon= (TextView) findViewById(R.id.food_price);
        mota =(TextView) findViewById(R.id.food_description);
        collapsingToolbarLayout =(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        //Nhận THông tin Food từ Intent gửi đến
        Intent intent = getIntent();
        if(intent != null){
            foodId       = intent.getStringExtra("FoodId");
            RestaurentID = intent.getStringExtra("RestaurentID");
        }
        if(!foodId.isEmpty() && foodId !=null && !RestaurentID.isEmpty() && RestaurentID!= null){
            getDetailFood(RestaurentID,foodId);
        }

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(food.getTinhTrang() == 1) {
                    Cart cart = new Cart(food.getTenMon(),food.getTenQuan(),food.getIdQuan(),food.getLinkAnh(),food.getGiaMon(),quantity,price);
                    //them vao database
                    mDatabase.child("Carts").child(userID).child(food.getTenMon()).setValue(cart);
                    Toast.makeText(FoodDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(FoodDetailActivity.this, "Món ăn đã hết", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Hiển thông thông tin chi tiết món
    private void getDetailFood(String restaurentID, String foodId) {
        // get data quan
        mDatabase.child("Users").child(restaurentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quanAn = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        //set data
        mDatabase.child("QuanAn").child(restaurentID).child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // đối tượng food lấy dữ liệu từ database
                food = dataSnapshot.getValue(MonAn.class);
                //THiết lập ảnh
                Picasso.with(getBaseContext()).load(food.getLinkAnh()).into(hinh);
                collapsingToolbarLayout.setTitle(food.getTenMon());

                price = food.getGiaMon();
                giamon.setText(price +" đ");
                number.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quantity = Long.parseLong(number.getNumber());
                        price = quantity * food.getGiaMon();
                        giamon.setText(price+" đ");
                    }
                });

                tenmon.setText(food.getTenMon());
                if (food.getTinhTrang() == 1) {
                    mota.setText("Tình trạng món ăn: Còn\nQuán "+food.getTenQuan()+"\nĐịa chỉ: "+quanAn.getAddress()+"\nLiên hệ: "+quanAn.getPhone());
                } else {
                    mota.setText("Tình trạng món ăn: Hết\nQuán "+food.getTenQuan()+"\nĐịa chỉ: "+quanAn.getAddress()+"\nLiên hệ: "+quanAn.getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


}


