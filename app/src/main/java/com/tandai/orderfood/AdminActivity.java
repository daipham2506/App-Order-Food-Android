package com.tandai.orderfood;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tandai.orderfood.Notifications.Token;

import io.paperdb.Paper;

public class AdminActivity extends AppCompatActivity {

    private Button ThemQuan,XoaQuan, viewUser;
    ImageView logOut,doiMK;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_admin);

        updateToken(FirebaseInstanceId.getInstance().getToken());

        Paper.init(this);



        ThemQuan    =(Button) findViewById(R.id.btnThemQuanKhungAdmin);
        XoaQuan     =(Button) findViewById(R.id.btnXoaQuanKhungAdmin);
        logOut      =(ImageView) findViewById(R.id.btnLogOutAdmin);
        doiMK       =(ImageView) findViewById(R.id.btnChangePassAdmin);
        viewUser = findViewById(R.id.btnViewListUser);

        ThemQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screenThemQuan = new Intent(AdminActivity.this, AddRestaurantActivity.class);
                startActivity(screenThemQuan);
            }
        });
        XoaQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screenXoaQuan = new Intent(AdminActivity.this, RemoveRestaurantActivity.class);
                startActivity(screenXoaQuan);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangXuat();
            }
        });
        doiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,ChangePassActivity.class));
            }
        });
        viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,ViewUserActivity.class));
            }
        });
    }


    private void DangXuat(){
        final Dialog dialogLogOut = new Dialog(AdminActivity.this,R.style.Theme_Dialog);
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
                startActivity(new Intent(AdminActivity.this,WelcomActivity.class));
            }
        });
    }


    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token,3);
        reference.child(user.getUid()).setValue(token1);
    }


}
