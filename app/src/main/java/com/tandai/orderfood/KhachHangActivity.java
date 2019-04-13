package com.tandai.orderfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class KhachHangActivity extends AppCompatActivity {
    TextView home,basket,person;
    TextView tim;
    ListView lvQuan;
    ArrayList<QuanAn> arrQuanAn;
    QuanAnAdapter adapter = null;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_khach_hang);
        AnhXa();
        arrQuanAn = new ArrayList<>();
        adapter= new QuanAnAdapter(this, R.layout.line_restaurent, arrQuanAn);
        lvQuan.setAdapter(adapter);
        LoadData();

        tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KhachHangActivity.this, SearchFood.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KhachHangActivity.this,InfoPersonActivity.class));
            }
        });

    }


    private void AnhXa(){
        tim     =   (TextView) findViewById(R.id.tvTim);
        lvQuan  =   (ListView) findViewById(R.id.listviewQuanAn);
        home    =   (TextView) findViewById(R.id.tvHome);
        basket  =   (TextView) findViewById(R.id.tvBasket);
        person  =   (TextView) findViewById(R.id.tvPerson);
    }

    private void LoadData(){
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    User uInfo = ds.getValue(User.class);
                    if(uInfo.getUserType().equals("restaurent")){
                        arrQuanAn.add(new QuanAn(uInfo.getName(),uInfo.getAddress(),uInfo.getPhone()));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(KhachHangActivity.this, "Not found", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener);
    }

}
