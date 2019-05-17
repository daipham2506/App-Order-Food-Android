package com.tandai.orderfood;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class WelcomActivity extends AppCompatActivity {
    Button btnLog;
    Button btnReg;
    DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuthencation =FirebaseAuth.getInstance();

    ProgressDialog process;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.layout_welcom);


        printKeyHash();


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



    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.tandai.orderfood"
                    , PackageManager.GET_SIGNATURES);

            for(Signature signature:info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash",Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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

                        final FirebaseUser USER = FirebaseAuth.getInstance().getCurrentUser();
                        String userID = USER.getUid();

                        mData = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

                        mData.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                process.dismiss();
                                User user   =   dataSnapshot.getValue(User.class);

                                if(user.getUserType().equals("admin")){
                                    startActivity(new Intent(WelcomActivity.this,AdminActivity.class));
                                }
                                else if(user.getUserType().equals("restaurent")){
                                    startActivity(new Intent(WelcomActivity.this,QuanAnActivity.class));
                                }
                                else if(user.getUserType().equals("customer")){

                                    if(USER.isEmailVerified()){
                                        startActivity(new Intent(WelcomActivity.this, KhachHangActivity.class));
                                    }
                                    else{
                                        Toast.makeText(WelcomActivity.this, "Vui lòng xác thực Email để đăng nhập", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    else {
                        process.dismiss();
                        Toast.makeText(WelcomActivity.this, "Tài khoản hoặc mật khẩu không hợp lệ.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}



