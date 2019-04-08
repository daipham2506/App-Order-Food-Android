package com.tandai.orderfood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SuaMonActivity extends AppCompatActivity {
    private EditText tenMon,giaMon,tinhTrang;
    private Button capnhat;
    private DatabaseReference mDatabase;
    int check = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sua_mon);
        AnhXa();
        capnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ten = tenMon.getText().toString().trim();
                final String stringGia=giaMon.getText().toString().trim();
                final long gia =Long.parseLong(stringGia);
                final String stringtt=tinhTrang.getText().toString().trim();
                final Integer tt = Integer.parseInt(stringtt);
                if(ten.isEmpty() || stringGia.isEmpty() || stringtt.isEmpty()){
                    Toast.makeText(SuaMonActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else{

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String userID = user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("QuanAn").child(userID);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                String name = ds.getKey();
                                if(name.equals(ten)){
                                    check = check + 1 ;
                                    break;
                                }
                            }
                            if( check == 0){
                                Toast.makeText(SuaMonActivity.this, "Tên món ăn không chính xác", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //set Value
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("QuanAn").child(userID).child(ten);
                                mDatabase.child("giaMon").setValue(gia);
                                mDatabase.child("tinhTrang").setValue(tt);
                                Toast.makeText(SuaMonActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SuaMonActivity.this,QuanAnActivity.class));
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    mDatabase.addListenerForSingleValueEvent(eventListener);







                }
            }
        });
    }

    private void AnhXa(){
        tenMon=(EditText) findViewById(R.id.edtTenMonLayoutSua);
        giaMon=(EditText) findViewById(R.id.edtGiaMonLayoutSua);
        tinhTrang=(EditText) findViewById(R.id.edtStatusMonLayoutSua);
        capnhat=(Button) findViewById(R.id.btnSuaMonLayoutSua);
    }
}
