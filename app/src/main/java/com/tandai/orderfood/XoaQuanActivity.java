package com.tandai.orderfood;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class XoaQuanActivity extends AppCompatActivity {
    DatabaseReference mData;
    FirebaseAuth mAuthencation;
    Button xoaQuan;
    EditText emailXoa;
    String Pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_xoa_quan);
        AnhXa();
        mAuthencation = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        xoaQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XoaQuan();
            }
        });

    }

    private void AnhXa(){
        xoaQuan =   (Button) findViewById(R.id.btnXoaQuan);
        emailXoa=   (EditText) findViewById(R.id.edtEmailXoaQuan);
    }

    private void XoaQuan(){
        final String Email = emailXoa.getText().toString().trim();

        // lay data của realtime bd từ email
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user   =   dataSnapshot.getValue(User.class);
                final String pass;
                if(user.getEmail().equals(Email) && user.getUserType().equals("retaurent")){

                    // xoa trong realtime db
                    dataSnapshot.getRef().setValue(null);
                    //xoa trong Auth ( chịu )
                    //Pass= user.getPass();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
       });

       }

}
