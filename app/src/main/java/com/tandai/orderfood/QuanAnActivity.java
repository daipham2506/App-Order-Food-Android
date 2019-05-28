package com.tandai.orderfood;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import io.paperdb.Paper;

public class QuanAnActivity extends AppCompatActivity {
    Button themMon,xoaMon,xemDSMon,suaMon, xemDonDatHang;
    ImageView LogOut,doiMK;
    TextView tenQuan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_quan_an);

        // Paper init
        Paper.init(this);


        AnhXa();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tenQuan.setText("Quán "+user.getDisplayName());
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangXuat();
            }
        });
        doiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanAnActivity.this,ChangePassActivity.class));
            }
        });
        themMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanAnActivity.this,ThemMonActivity.class));
            }
        });
        suaMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanAnActivity.this,SuaMonActivity.class));
            }
        });
        xoaMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanAnActivity.this,XoaMonActivity.class));
            }
        });
        xemDSMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanAnActivity.this,XemDSMonActivity.class));
            }
        });

        xemDonDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanAnActivity.this,QuanAnXemDSDonDatHang.class));
            }
        });


    }


    private void AnhXa(){
        LogOut=(ImageView) findViewById(R.id.btnLogOutQuan);
        doiMK=(ImageView) findViewById(R.id.btnChangePassQuan);
        tenQuan=(TextView) findViewById(R.id.twTenQuan);
        themMon=(Button) findViewById(R.id.btnThemMon);
        xoaMon=(Button) findViewById(R.id.btnXoaMon);
        suaMon=(Button) findViewById(R.id.btnSuaMon);
        xemDSMon=(Button) findViewById(R.id.btnXemMon);
        xemDonDatHang= (Button) findViewById(R.id.btnXemDonHang);
    }

    private void DangXuat(){
        final Dialog dialogLogOut = new Dialog(QuanAnActivity.this);
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
                //delete remember user and password
                Paper.book().destroy();
                dialogLogOut.cancel();
                startActivity(new Intent(QuanAnActivity.this,WelcomActivity.class));
            }
        });
    }


}
