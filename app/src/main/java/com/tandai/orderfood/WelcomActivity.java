package com.tandai.orderfood;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import io.paperdb.Paper;


public class WelcomActivity extends AppCompatActivity {
    Button btnLog;
    Button btnReg;
    DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuthencation =FirebaseAuth.getInstance();
    ProgressDialog process;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_welcom);
        process = new ProgressDialog(WelcomActivity.this);
        process.setMessage("Vui lòng đợi");

        //Paper init
        Paper.init(this);

        //kiểm tra kết nối
        if(isNetworkAvailable()){
            Xuli();
        }
        else{
            final Dialog dialog   = new Dialog(this);
            dialog.setContentView(R.layout.layout_internet);
            Button btnThoat =   (Button) dialog.findViewById(R.id.btnThoatDiaLogInternet);
            Button btnConnect= (Button) dialog.findViewById(R.id.btnBatWifi);
            dialog.show();

            btnThoat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    finish();
                    System.exit(0);
                }
            });
            btnConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                    Xuli();
                }
            });
        }

    }


    // kiểm tra kết nối internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void Xuli(){
        //Anh xa
        btnLog = (Button) findViewById(R.id.btnLoginWelcom);
        btnReg = (Button) findViewById(R.id.btnRegisterWelcom);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomActivity.this, LoginActivity.class));
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomActivity.this, RegisterActivity.class));
            }
        });
        String user = Paper.book().read(Common.USER_KEY);
        String pass = Paper.book().read(Common.PWD_KEY);
        if(user != null && pass != null){
            if(!user.isEmpty() && !pass.isEmpty()){
                login(user,pass);
            }
        }
    }

    private void login(final String email, String pass) {
        process.show();
        if (email.isEmpty() || pass.isEmpty() ) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin. ", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuthencation.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        process.dismiss();
                        mData.child("Users").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                User user   =   dataSnapshot.getValue(User.class);

                                if(user.getUserType().equals("admin") && user.getEmail().equals(email)){
                                    startActivity(new Intent(WelcomActivity.this,AdminActivity.class));
                                }
                                else if(user.getUserType().equals("restaurent") && user.getEmail().equals(email)){
                                    startActivity(new Intent(WelcomActivity.this,QuanAnActivity.class));
                                }
                                else if(user.getUserType().equals("customer") && user.getEmail().equals(email)){
                                    startActivity(new Intent(WelcomActivity.this, KhachHangActivity.class));
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
                        Toast.makeText(WelcomActivity.this, "Tài khoản hoặc mật khẩu không hợp lệ.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}



