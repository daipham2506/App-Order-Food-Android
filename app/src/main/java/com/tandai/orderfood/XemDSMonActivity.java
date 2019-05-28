package com.tandai.orderfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class XemDSMonActivity extends AppCompatActivity {
    ImageView back;
    TextView tenquan;
    ListView lvFood;
    ArrayList<Food> arrFood;
    FoodAdapter adapter = null;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("QuanAn").child(userID);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_xem_dsmon);
        AnhXa();
        tenquan.setText("Quán "+ user.getDisplayName());
        arrFood = new ArrayList<>();
        adapter= new FoodAdapter(this, R.layout.line_food_xemds, arrFood);
        lvFood.setAdapter(adapter);
        LoadData();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(XemDSMonActivity.this, QuanAnActivity.class));
            }
        });
    }

    private void AnhXa(){
        back = (ImageView) findViewById(R.id.btnback);
        lvFood =(ListView) findViewById(R.id.listviewFood);
        tenquan =(TextView) findViewById(R.id.tvtenQuanLayoutXemDSMon);
    }

    private void LoadData(){
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    MonAn mon = ds.getValue(MonAn.class);
                    arrFood.add(new Food(mon.getTenMon(),mon.getTenQuan(),mon.getLinkAnh(),mon.getIdQuan(),mon.getGiaMon(),mon.getTinhTrang()));
                    //Toast.makeText(XemDSMonActivity.this, mon.getTenMon(), Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(XemDSMonActivity.this, "Not found", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener);
    }


}
