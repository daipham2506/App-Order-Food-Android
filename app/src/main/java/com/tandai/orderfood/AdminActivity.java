package com.tandai.orderfood;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import io.paperdb.Paper;

public class AdminActivity extends AppCompatActivity {

    private Button ThemQuan,XoaQuan;
    ImageView logOut,doiMK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_admin);

        Paper.init(this);

        ThemQuan    =(Button) findViewById(R.id.btnThemQuanKhungAdmin);
        XoaQuan     =(Button) findViewById(R.id.btnXoaQuanKhungAdmin);
        logOut      =(ImageView) findViewById(R.id.btnLogOutAdmin);
        doiMK       =(ImageView) findViewById(R.id.btnChangePassAdmin);

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
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete remember user and password
                Paper.book().destroy();
                DangXuat();
            }
        });
        doiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,ChangePassActivity.class));
            }
        });
    }


    private void DangXuat(){
        final Dialog dialogLogOut = new Dialog(AdminActivity.this);
        dialogLogOut.setContentView(R.layout.dialog_dang_xuat);
        dialogLogOut.show();
        Button khong=(Button) dialogLogOut.findViewById(R.id.btnKhongDialogDangXuat);
        Button thoat=(Button) dialogLogOut.findViewById((R.id.btnDialogDangXuat));
        khong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLogOut.cancel();
            }
        });
        thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLogOut.cancel();
                startActivity(new Intent(AdminActivity.this,WelcomActivity.class));
            }
        });
    }


}
