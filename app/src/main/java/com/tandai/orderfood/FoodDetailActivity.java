package com.tandai.orderfood;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    //Context context;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String foodId = "";
    String RestaurentID = "";
    FloatingActionButton btnCart;
    ElegantNumberButton number;
    ImageView hinh;
    TextView tenmon,giamon,mota;
    DatabaseReference mDatabase;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_food_detail);

        //Firebase
        mDatabase  = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
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

        if(!foodId.isEmpty() && foodId != null && !RestaurentID.isEmpty() && RestaurentID != null){
            getDetailFood(RestaurentID,foodId);
        }
    }

    //Hiển thông thông tin chi tiết món
    private void getDetailFood(String restaurentID, String foodId) {
        mDatabase  = FirebaseDatabase.getInstance().getReference().child("QuanAn").child(restaurentID).child(foodId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // đối tượng food lấy dữ liệu từ database
                MonAn food = dataSnapshot.getValue(MonAn.class);
                //THiết lập ánh
                Picasso.with(getBaseContext()).load(food.getLinkAnh()).into(hinh);
                collapsingToolbarLayout.setTitle(food.getTenMon());
                giamon.setText(food.getGiaMon()+"đ");
                if (food.getTinhTrang() == 1) {
                    mota.setText("Quán " + food.getTenQuan() + "\nTình trạng món ăn: Còn");
                } else {
                    mota.setText("Quán " + food.getTenQuan() + "\nTình trạng món ăn: Hết");
                }
                tenmon.setText(food.getTenMon());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


