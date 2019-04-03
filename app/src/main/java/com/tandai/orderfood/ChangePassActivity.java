package com.tandai.orderfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.support.annotation.NonNull;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ChangePassActivity extends AppCompatActivity {
    private EditText NhapMK,NhapLaiMK;
    private Button doi,dong;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_pass);
        AnhXa();
        changePass();
    }

    private void AnhXa(){
        NhapMK = (EditText) findViewById(R.id.edtNhapPass);
        NhapLaiMK=(EditText) findViewById(R.id.edtNhaplaiPass);
        doi=(Button) findViewById(R.id.btnDoiPass);
        dong=(Button) findViewById(R.id.btnDongChangePass);
    }

    private void changePass(){


        doi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String MK = NhapMK.getText().toString().trim();
                final String MK1 = NhapLaiMK.getText().toString().trim();
                if(MK1.isEmpty() || MK.isEmpty()){
                    Toast.makeText(ChangePassActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(MK.equals(MK1)){
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        final String userID =user.getUid();
                        final DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
                        user.updatePassword(MK)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangePassActivity.this, "Bạn đã đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                            // updata pass in database
                                            mDatabase.child("Users").child(userID).child("pass").setValue(MK);
                                            //ve man hinh chinh
                                            if(user.getEmail().equals("admin@gmail.com")){
                                                startActivity(new Intent(ChangePassActivity.this,AdminActivity.class));
                                            }
                                            else {
                                                startActivity(new Intent(ChangePassActivity.this, QuanAnActivity.class));
                                            }
                                        }
                                        else{
                                            Toast.makeText(ChangePassActivity.this, "Đổi không thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else{
                        Toast.makeText(ChangePassActivity.this, "Mật khẩu nhập lại không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePassActivity.this,QuanAnActivity.class));
            }
        });
    }
}
