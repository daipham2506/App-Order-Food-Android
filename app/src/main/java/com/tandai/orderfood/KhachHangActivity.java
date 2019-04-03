package com.tandai.orderfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class KhachHangActivity extends AppCompatActivity {
    Button home,basket,person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_khach_hang);
        AnhXa();
    }


    private void AnhXa(){
        home    =   (Button) findViewById(R.id.btnHome);
        basket  =   (Button) findViewById(R.id.btnBasket);
        person  =   (Button) findViewById(R.id.btnPerson);
    }

}
