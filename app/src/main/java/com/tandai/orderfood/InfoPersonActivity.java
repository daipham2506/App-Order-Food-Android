package com.tandai.orderfood;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class InfoPersonActivity extends AppCompatActivity {
    private Button updateInfo;
    private ImageView logout, home;
    private TextView ten, tenTK, diachi, sdt;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_info_person);

        //set color status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        AnhXa();
        LoadData();

        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog   = new Dialog(InfoPersonActivity.this,R.style.Theme_Dialog);
                dialog.setContentView(R.layout.dialog_update_info);
                dialog.show();
                final EditText name = (EditText) dialog.findViewById(R.id.updateName);
                final EditText address = (EditText) dialog.findViewById(R.id.updateAddress);
                final EditText phone = (EditText) dialog.findViewById(R.id.updatePhone);
                Button update = (Button) dialog.findViewById(R.id.btnUpdate);
                Button cancel = (Button) dialog.findViewById(R.id.btnCancel);

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        name.setText(user.getName());
                        address.setText(user.getAddress());
                        phone.setText(user.getPhone());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String Name = name.getText().toString();
                        final String Address = address.getText().toString();
                        final String Phone = phone.getText().toString();
                        if(Name.isEmpty() || Address.isEmpty() || Phone.isEmpty()){
                            Toast.makeText(InfoPersonActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(InfoPersonActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("name").setValue(Name);
                                    mDatabase.child("address").setValue(Address);
                                    mDatabase.child("phone").setValue(Phone);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoPersonActivity.this, KhachHangActivity.class));
            }
        });

    }

    private void AnhXa(){
        updateInfo = (Button) findViewById(R.id.btnUpdateInfo);
        ten     = (TextView) findViewById(R.id.tvtenKhachHang);
        tenTK     = (TextView) findViewById(R.id.tvtentaikhoan);
        diachi     = (TextView) findViewById(R.id.tvdiachikhachhang);
        sdt     = (TextView) findViewById(R.id.tvsdtkhachhang);
        home    =(ImageView) findViewById(R.id.ivhome);
    }

    private void LoadData(){
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User uInfo = dataSnapshot.getValue(User.class);
                ten.setText("Tên khách hàng: "+ uInfo.getName());
                tenTK.setText("Tên tài khoản: "+uInfo.getEmail());
                diachi.setText("Địa chỉ: "+uInfo.getAddress());
                sdt.setText("Số điện thoại: "+uInfo.getPhone());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(eventListener);
    }


}
