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

public class QuanAnActivity extends AppCompatActivity {
    Button LogOut;
    TextView tenQuan;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_quan_an);
        AnhXa();
        //getName();
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangXuat();
            }
        });
    }


    private void AnhXa(){
        LogOut=(Button) findViewById(R.id.btnLogOutQuan);
        tenQuan=(TextView) findViewById(R.id.twTenQuan);
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
                dialogLogOut.cancel();
                startActivity(new Intent(QuanAnActivity.this,WelcomActivity.class));
            }
        });
    }

    private void getName(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = firebaseUser.getUid();
        ref =   FirebaseDatabase.getInstance().getReference();




//        mData.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                for(DataSnapshot ds : dataSnapshot.getChildren() ){
//                    uInfo.setName(ds.child(userID).getValue(User.class).getName()); //set the name
//                    uInfo.setEmail(ds.child(userID).getValue(User.class).getEmail()); //set the email
//                    uInfo.setUserType(ds.child(userID).getValue(User.class).getUserType()); //set the email
//                    uInfo.setAddress(ds.child(userID).getValue(User.class).getAddress()); //set the email
//                    uInfo.setPhone(ds.child(userID).getValue(User.class).getPhone()); //set the email
//                    uInfo.setPass(ds.child(userID).getValue(User.class).getPass()); //set the email
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        tenQuan.setText(uInfo.getName());


    }

}
