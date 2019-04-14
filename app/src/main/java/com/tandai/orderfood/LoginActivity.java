package com.tandai.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    DatabaseReference mData;
    FirebaseAuth mAuthencation;
    Button btnLog;
    EditText email;
    EditText pass;
    Button btnThoat;
    ProgressDialog process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        AnhXa();
        process = new ProgressDialog(LoginActivity.this);
        process.setMessage("Vui lòng đợi");
        mAuthencation = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screenWel = new Intent(LoginActivity.this,WelcomActivity.class);
                startActivity(screenWel);
            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                process.show();
                DangNhap();
            }
        });
    }

    private void AnhXa(){
        btnLog=(Button) findViewById(R.id.btnLog);
        email= (EditText) findViewById(R.id.edtEmailLog);
        pass=(EditText) findViewById((R.id.edtPassLog));
        btnThoat =(Button) findViewById(R.id.btnThoatLog);
    }

    private void DangNhap(){
        final String Email = email.getText().toString().trim();
        String Pass = pass.getText().toString().trim();
        if (Email.isEmpty() || Pass.isEmpty() ) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin. ", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuthencation.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        process.dismiss();
                        mData.child("Users").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                User user   =   dataSnapshot.getValue(User.class);

                                if(user.getUserType().equals("admin") && user.getEmail().equals(Email)){
                                    startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                                }
                                else if(user.getUserType().equals("restaurent") && user.getEmail().equals(Email)){
                                    startActivity(new Intent(LoginActivity.this,QuanAnActivity.class));
                                }
                                else if(user.getUserType().equals("customer") && user.getEmail().equals(Email)){
                                    startActivity(new Intent(LoginActivity.this,TestActivity.class));
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
                    } else {
                        process.dismiss();
                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không hợp lệ.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}
