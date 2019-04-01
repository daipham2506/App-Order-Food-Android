package com.tandai.orderfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    Button ThemQuan;
    Button XoaQuan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_admin);
        ThemQuan    =(Button) findViewById(R.id.btnThemQuanKhungAdmin);
        XoaQuan     =(Button) findViewById(R.id.btnXoaQuanKhungAdmin);

        ThemQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screenThemQuan = new Intent(AdminActivity.this,ThemQuanActivity.class);
                startActivity(screenThemQuan);
            }
        });
        XoaQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screenXoaQuan = new Intent(AdminActivity.this,XoaQuanActivity.class);
                startActivity(screenXoaQuan);
            }
        });
    }
}
