package com.tandai.orderfood;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText email,pass,name,phone,address;
    Button btnDangKy;
    FirebaseAuth mAuthencation;
    DatabaseReference mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        Button btnThoat =(Button) findViewById(R.id.btnThoatReg);
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screenWel1 = new Intent(RegisterActivity.this,WelcomActivity.class);
                startActivity(screenWel1);
            }
        });

        AnhXa();
        mAuthencation = FirebaseAuth.getInstance();
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangKy();
            }
        });
        mData = FirebaseDatabase.getInstance().getReference();


    }
    private void AnhXa(){
        email = (EditText) findViewById(R.id.txtEmail);
        pass = (EditText) findViewById(R.id.txtPass);
        name = (EditText) findViewById(R.id.txtName);
        phone = (EditText) findViewById(R.id.txtPhone);
        address = (EditText) findViewById(R.id.txtAddress);
        btnDangKy=(Button) findViewById(R.id.btnReg);

    }


    private void DangKy() {
        String Email = email.getText().toString().trim(); //trim() bỏ khoảng trống ở đầu và cuối chuỗi
        String Pass = pass.getText().toString().trim();
        String Name = name.getText().toString().trim();
        String Phone = phone.getText().toString().trim();
        String Address = address.getText().toString().trim();
        final User KhachHang    = new User(Email,Pass,Name,Phone,Address,"customer");

        if (Email.isEmpty() || Pass.isEmpty() || Name.isEmpty() || Phone.isEmpty() || Address.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin. ", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuthencation.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công.", Toast.LENGTH_SHORT).show();
                        //push data len realtime database
                        mData.child("Users").push().setValue(KhachHang);
                        //chuyen ve man hinh dang nhap
                        Intent screenLog = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(screenLog);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
