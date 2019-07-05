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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tandai.orderfood.Model.User;

import dmax.dialog.SpotsDialog;

public class AddRestaurantActivity extends AppCompatActivity {
    EditText email,pass,name,phone,address;
    Button btnThem;
    FirebaseAuth mAuthencation;
    DatabaseReference mData;
    FirebaseUser user;
    AlertDialog waiting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_restaurant);
        AnhXa();
        waiting =  new SpotsDialog.Builder().setContext(this).setMessage("Vui lòng đợi...").setCancelable(false).build();
        mAuthencation = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemQuan();
            }
        });
    }

    private void AnhXa(){
        email   = (EditText) findViewById(R.id.edtEmailQuan);
        pass    = (EditText) findViewById(R.id.edtPassQuan);
        name    = (EditText) findViewById(R.id.edtTenQuan);
        phone   = (EditText) findViewById(R.id.edtPhoneQuan);
        address = (EditText) findViewById(R.id.edtAddressQuan);
        btnThem = ( Button) findViewById(R.id.btnThemQuan);
    }

    private void ThemQuan(){
        waiting.show();
        String Email = email.getText().toString().trim();
        String Pass = pass.getText().toString().trim();
        final String Name = name.getText().toString().trim();
        String Phone = phone.getText().toString().trim();
        String Address = address.getText().toString().trim();
        final User QuanAn    = new User(Email,Pass,Name,Phone,Address,"restaurent");
        if (Email.isEmpty() || Pass.isEmpty() || Name.isEmpty() || Phone.isEmpty() || Address.isEmpty()) {
            waiting.dismiss();
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin. ", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuthencation.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        waiting.dismiss();
                        Toast.makeText(AddRestaurantActivity.this, "Thêm tài khoản Quán ăn thành công.", Toast.LENGTH_SHORT).show();
                        user    =   mAuthencation.getCurrentUser();
                        //set Name cho user
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(Name)
                                .setPhotoUri(null)
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                        //push data len realtime database
                        String userID   =   user.getUid();
                        mData.child("Users").child(userID).setValue(QuanAn);
                        //chuyen ve man hinh Admin
                        startActivity(new Intent(AddRestaurantActivity.this,AdminActivity.class));
                    } else {
                        waiting.dismiss();
                        Toast.makeText(AddRestaurantActivity.this, "Tài khoản đã tồn tại.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
