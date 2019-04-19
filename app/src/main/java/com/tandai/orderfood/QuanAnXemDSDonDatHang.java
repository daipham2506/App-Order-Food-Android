package com.tandai.orderfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

public class QuanAnXemDSDonDatHang extends AppCompatActivity {

    ListView listOrder;
    ArrayList<Order> arrOrder;
    RestaurentViewOrderAdapter adapter = null;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    DatabaseReference mDatabase;
    FButton btnThoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_quan_an_xem_dsdon_dat_hang);

        btnThoat =(FButton) findViewById(R.id.btnThoatXemdonhangQuanan);
        listOrder  =   (ListView) findViewById(R.id.listRestaurent_viewOrder);
        arrOrder = new ArrayList<>();
        adapter = new RestaurentViewOrderAdapter(this, R.layout.line_restaurent_view_order, arrOrder);
        listOrder.setAdapter(adapter);
        getInfoOrder();

        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanAnXemDSDonDatHang.this, QuanAnActivity.class));
            }
        });
    }


    private void getInfoOrder(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Orders").child(userID);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Order order = ds.getValue(Order.class);
                    arrOrder.add(order);
                    adapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener);
    }
}
