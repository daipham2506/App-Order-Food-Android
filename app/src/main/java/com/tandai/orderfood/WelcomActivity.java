package com.tandai.orderfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class WelcomActivity extends AppCompatActivity {
    Button btnLog;
    Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_welcom);

        //Anh xa
        btnLog=(Button) findViewById(R.id.btnLoginWelcom);
        btnReg=(Button) findViewById(R.id.btnRegisterWelcom);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screenLog = new Intent(WelcomActivity.this,LoginActivity.class);
                startActivity(screenLog);
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screenReg = new Intent(WelcomActivity.this,RegisterActivity.class);
                startActivity(screenReg);
            }
        });
    }
}
