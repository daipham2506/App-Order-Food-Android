package com.tandai.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    DatabaseReference mData;
    FirebaseAuth mAuthencation;
    Button btnLog;
    EditText email;
    EditText pass;
    ProgressDialog process;
    CheckBox Remember;
    TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        AnhXa();
        process = new ProgressDialog(LoginActivity.this);
        process.setMessage("Vui lòng đợi");
        mAuthencation = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference().child("Users");

        Paper.init(this);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangNhap();
            }
        });

       forgotPass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              startActivity(new Intent(LoginActivity.this,ForgotPassActivity.class));
           }
       });
    }

    private void AnhXa(){
        btnLog=(Button) findViewById(R.id.btnLog);
        email= (EditText) findViewById(R.id.edtEmailLog);
        pass=(EditText) findViewById((R.id.edtPassLog));
        Remember =(CheckBox) findViewById(R.id.ckbRemember);
        forgotPass = (TextView) findViewById(R.id.forgotPass);
    }

    private void DangNhap(){
        final String Email = email.getText().toString().trim();
        final String Pass = pass.getText().toString().trim();
        if (Email.isEmpty() || Pass.isEmpty() ) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin. ", Toast.LENGTH_SHORT).show();
        }
        else {
            process.show();
            if(Remember.isChecked()){
                Paper.book().write(Common.USER_KEY,Email);
                Paper.book().write(Common.PWD_KEY,Pass);
            }
            mAuthencation.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        process.dismiss();
                        mData.addChildEventListener(new ChildEventListener() {
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
                                    startActivity(new Intent(LoginActivity.this, KhachHangActivity.class));
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
                        // ghi lai mk trong database neu quen mat kau sau khi lay lai
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userID = user.getUid();
                        DatabaseReference mDatabase =FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.child("pass").getRef().setValue(Pass);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        mDatabase.addListenerForSingleValueEvent(eventListener);

                    } else {
                        process.dismiss();
                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không hợp lệ.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}
