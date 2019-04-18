package com.tandai.orderfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

public class CartActivity extends AppCompatActivity {
    ListView lvCart;
    ArrayList<Cart> arrCart;
    CartAdapter adapter = null;
    TextView total;
    FButton btnDatHang;
    long tongTien = 0;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cart);

        total =(TextView) findViewById(R.id.total);
        btnDatHang =(FButton) findViewById(R.id.btnPlaceOrder);

        lvCart  =   (ListView) findViewById(R.id.listCart);
        arrCart = new ArrayList<>();
        adapter = new CartAdapter(this, R.layout.line_cart, arrCart);
        lvCart.setAdapter(adapter);

        getDataCart();



    }

    private void getDataCart(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Carts").child(userID);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Cart cart = ds.getValue(Cart.class);
                    arrCart.add(cart);
                    tongTien = tongTien + cart.getTongTien();
                    adapter.notifyDataSetChanged();
                }
                total.setText(String.valueOf(tongTien)+ "đ");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener);
    }


}
