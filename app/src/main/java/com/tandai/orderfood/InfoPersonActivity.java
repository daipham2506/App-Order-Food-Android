package com.tandai.orderfood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tandai.orderfood.Model.MonAn;
import com.tandai.orderfood.Model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;


public class InfoPersonActivity extends AppCompatActivity {
    Button updateInfo;
    CircleImageView image;
    ImageView change_image;
    TextView ten, tenTK, diachi, sdt;
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID;
    DatabaseReference mDatabase;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://databaseorderfood.appspot.com");
    int REQUEST_CODE_FOLDER = 123;
    String link_image;
    AlertDialog waiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_info_person);

        Intent intent = getIntent();
        userID       = intent.getStringExtra("userID");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        AnhXa();
        LoadData();

        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdate();
            }

        });

        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_FOLDER);
            }
        });
    }

    // get image from folder
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                waiting.show();
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);

                Calendar calendar = Calendar.getInstance();
                String tenhinh="image"+calendar.getTimeInMillis();
                final StorageReference mountainsRef = storageRef.child(tenhinh+".png");
                image.setDrawingCacheEnabled(true);
                image.buildDrawingCache();

                Bitmap bitmap1 = ((BitmapDrawable) image.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data1 = baos.toByteArray();

                final UploadTask uploadTask = mountainsRef.putBytes(data1);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // get downloadUrl
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                link_image = mountainsRef.getDownloadUrl().toString();
                                // Continue with the task to get the download URL
                                return mountainsRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    link_image = task.getResult().toString();
                                    mDatabase.child("image").setValue(link_image);
                                    Toast.makeText(InfoPersonActivity.this, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(InfoPersonActivity.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                                }
                                waiting.dismiss();
                            }
                        });
                    }
                });


            } catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showDialogUpdate(){
        final Dialog dialog   = new Dialog(InfoPersonActivity.this,R.style.Theme_Dialog);
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
                    Toast.makeText(InfoPersonActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(InfoPersonActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    mDatabase.child("name").setValue(Name);
                    mDatabase.child("address").setValue(Address);
                    mDatabase.child("phone").setValue(Phone);
                }
            }
        });
    }

    private void AnhXa(){
        updateInfo  = findViewById(R.id.btnUpdateInfo);
        ten         = findViewById(R.id.tvtenKhachHang);
        tenTK       = findViewById(R.id.tvtentaikhoan);
        diachi       =  findViewById(R.id.tvdiachikhachhang);
        sdt          = findViewById(R.id.tvsdtkhachhang);
        image       = findViewById(R.id.image_person);
        change_image = findViewById(R.id.change_image_person);
        waiting =  new SpotsDialog.Builder().setContext(this).setMessage("Đang upload...").setCancelable(false).build();
    }

    private void LoadData(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User uInfo = dataSnapshot.getValue(User.class);
                ten.setText( uInfo.getName());
                tenTK.setText(uInfo.getEmail());
                diachi.setText(uInfo.getAddress());
                sdt.setText(uInfo.getPhone());
                if(uInfo.getImage() != null){
                    Picasso.with(getApplicationContext()).load(uInfo.getImage()).into(image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
