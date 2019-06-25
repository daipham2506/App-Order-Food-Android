package com.tandai.orderfood;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import info.hoang8f.widget.FButton;

public class ForgotPassActivity extends AppCompatActivity {
    EditText email;
    FButton btnGetPass;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forgot_pass);

        //set color status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        //anh xa
        email = (EditText) findViewById(R.id.EmailForgotPass);
        btnGetPass = (FButton) findViewById(R.id.btnGetPass);
        firebaseAuth =FirebaseAuth.getInstance();
        //xu li
        btnGetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                if(Email.isEmpty()) {
                    Toast.makeText(ForgotPassActivity.this, "Vui lòng điền Email", Toast.LENGTH_SHORT).show();
                }
                else {

                    firebaseAuth.sendPasswordResetEmail(Email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPassActivity.this, "Mở Email để đổi mật khẩu mới", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ForgotPassActivity.this, LoginActivity.class));
                                    } else {
                                        Toast.makeText(ForgotPassActivity.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }


}
