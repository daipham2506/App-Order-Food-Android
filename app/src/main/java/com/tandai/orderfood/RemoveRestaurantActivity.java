package com.tandai.orderfood;


import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tandai.orderfood.Model.User;

import dmax.dialog.SpotsDialog;

public class RemoveRestaurantActivity extends AppCompatActivity {
    DatabaseReference mData, database;
    Button xoaQuan;
    EditText emailXoa;
    String Pass;
    String userID = "";
    AlertDialog waiting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_remove_restaurant);
        AnhXa();
        waiting =  new SpotsDialog.Builder().setContext(this).setMessage("Vui lòng đợi").setCancelable(false).build();
        xoaQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waiting.show();
                XoaQuan();
            }
        });

    }

    private void AnhXa(){
        xoaQuan =   (Button) findViewById(R.id.btnXoaQuan);
        emailXoa=   (EditText) findViewById(R.id.edtEmailXoaQuan);
    }

    private void XoaQuan() {
        final String Email = emailXoa.getText().toString().trim();

        // lay data của realtime bd từ email
        mData = FirebaseDatabase.getInstance().getReference().child("Users");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if (ds.getValue() != null){
                        User user = ds.getValue(User.class);
                        if(user.getEmail().equals(Email)){
                            userID = ds.getKey();
                            database = FirebaseDatabase.getInstance().getReference().child("QuanAn");
                            database.child(userID).setValue(null);
                            waiting.dismiss();
                            Toast.makeText(RemoveRestaurantActivity.this, "Bạn đã xóa thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RemoveRestaurantActivity.this,AdminActivity.class));
                            return;
                        }
                    }
                }
                waiting.dismiss();
                Toast.makeText(RemoveRestaurantActivity.this, "Email quán không tồn tại", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
