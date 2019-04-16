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

public class XoaMonActivity extends AppCompatActivity {
    private Button xoa;
    private EditText tenMon;
    private DatabaseReference mDatabase;
    int check = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_xoa_mon);
        xoa =(Button) findViewById(R.id.btnXoaMonLayoutXoa);
        tenMon=(EditText) findViewById(R.id.edtTenMonLayoutXoa);

        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ten = tenMon.getText().toString().trim();
                if(ten.isEmpty()){
                    Toast.makeText(XoaMonActivity.this, "Vui lòng điền tên món cần xóa", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(XoaMonActivity.this, "Tên món ăn không chính xác", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //set null Value
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("QuanAn").child(userID).child(ten);
                                mDatabase.setValue(null);
                                Toast.makeText(XoaMonActivity.this, "Bạn đã xóa món ăn thành công ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(XoaMonActivity.this,QuanAnActivity.class));
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
}
