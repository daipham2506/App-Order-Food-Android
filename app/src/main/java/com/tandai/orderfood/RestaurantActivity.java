package com.tandai.orderfood;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.tandai.orderfood.Model.User;
import com.tandai.orderfood.Notifications.Token;

import io.paperdb.Paper;

public class RestaurantActivity extends AppCompatActivity {
    Button themMon,xemDSMon, xemDonDatHang,doiMK,update;
    ImageView LogOut;
    TextView tenQuan;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_restaurant);

        updateToken(FirebaseInstanceId.getInstance().getToken());

        // Paper init
        Paper.init(this);


        AnhXa();
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
                startActivity(new Intent(RestaurantActivity.this,ChangePassActivity.class));
            }
        });
        themMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantActivity.this, AddFoodActivity.class));
            }
        });

        xemDSMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantActivity.this, ViewListFoodActivity.class));
            }
        });

        xemDonDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantActivity.this, RestaurantViewOrderActivity.class));
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdate();
            }
        });


    }


    private void AnhXa(){
        LogOut=(ImageView) findViewById(R.id.btnLogOutQuan);
        doiMK=(Button) findViewById(R.id.btnChangePassQuan);
        tenQuan=(TextView) findViewById(R.id.twTenQuan);
        themMon=(Button) findViewById(R.id.btnThemMon);
        xemDSMon=(Button) findViewById(R.id.btnXemMon);
        xemDonDatHang= (Button) findViewById(R.id.btnXemDonHang);
        update= findViewById(R.id.btnUpdateInfoRestaurant);
    }

    private void DangXuat(){
        final Dialog dialogLogOut = new Dialog(RestaurantActivity.this,R.style.Theme_Dialog);
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
                startActivity(new Intent(RestaurantActivity.this,WelcomActivity.class));
            }
        });
    }


    private void showDialogUpdate(){
        final Dialog dialog   = new Dialog(RestaurantActivity.this,R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_update_info);
        dialog.show();
        final EditText name = (EditText) dialog.findViewById(R.id.updateName);
        final EditText address = (EditText) dialog.findViewById(R.id.updateAddress);
        final EditText phone = (EditText) dialog.findViewById(R.id.updatePhone);
        Button update = (Button) dialog.findViewById(R.id.btnUpdate);
        Button cancel = (Button) dialog.findViewById(R.id.btnCancel);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Toast.makeText(RestaurantActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RestaurantActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    mDatabase.child("name").setValue(Name);
                    mDatabase.child("address").setValue(Address);
                    mDatabase.child("phone").setValue(Phone);
                }
            }
        });
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token,2);
        reference.child(user.getUid()).setValue(token1);
    }


}
