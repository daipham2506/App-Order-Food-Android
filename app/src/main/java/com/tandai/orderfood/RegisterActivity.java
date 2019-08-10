package com.tandai.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tandai.orderfood.Model.Common;
import com.tandai.orderfood.Model.User;
import com.tandai.orderfood.Notifications.APIService;
import com.tandai.orderfood.Notifications.MyResponse;
import com.tandai.orderfood.Notifications.Notification;
import com.tandai.orderfood.Notifications.Sender;
import com.tandai.orderfood.Notifications.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText email,pass,name,phone,address;
    Button btnDangKy;
    FirebaseAuth mAuthencation;
    DatabaseReference mData;
    FirebaseUser user;
    ProgressDialog process;
    APIService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        mService = Common.getFCMService();
        process = new ProgressDialog(RegisterActivity.this);
        process.setMessage("Vui lòng đợi");
        AnhXa();
        mAuthencation = FirebaseAuth.getInstance();
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process.show();
                DangKy();
            }
        });
        mData = FirebaseDatabase.getInstance().getReference();


    }
    private void AnhXa(){
        email = (EditText) findViewById(R.id.txtEmail);
        pass = (EditText) findViewById(R.id.txtPass);
        name = (EditText) findViewById(R.id.txtName);
        phone = (EditText) findViewById(R.id.txtPhone);
        address = (EditText) findViewById(R.id.txtAddress);
        btnDangKy=(Button) findViewById(R.id.btnReg);

    }


    private void DangKy() {
        String Email = email.getText().toString().trim(); //trim() bỏ khoảng trống ở đầu và cuối chuỗi
        String Pass = pass.getText().toString().trim();
        final String Name = name.getText().toString().trim();
        String Phone = phone.getText().toString().trim();
        String Address = address.getText().toString().trim();
        final User KhachHang    = new User(Email,Pass,Name,Phone,Address,"customer");

        if (Email.isEmpty() || Pass.isEmpty() || Name.isEmpty() || Phone.isEmpty() || Address.isEmpty()) {
            process.dismiss();
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin. ", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuthencation.createUserWithEmailAndPassword(Email, Pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        user = mAuthencation.getCurrentUser();

                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            process.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công. Vui lòng xác thực Email", Toast.LENGTH_SHORT).show();

                                            // send notification to Admin
                                            sendNotification(Name);

                                            //set Name cho user
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(Name)
                                                    .setPhotoUri(null)
                                                    .build();
                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                        }
                                                    });
                                            //push data len realtime database
                                            String userID= user.getUid();
                                            mData.child("Users").child(userID).setValue(KhachHang);
                                            //chuyen ve man hinh chinh
                                            startActivity(new Intent(RegisterActivity.this,WelcomActivity.class));

                                        }
                                        else{
                                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });



                    }
                    else {
                        process.dismiss();
                        Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



    private void sendNotification(final String name){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = tokens.orderByChild("checkToken").equalTo(3); // get node isServerToken is 3
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Token token = ds.getValue(Token.class);
                    Notification notification = new Notification(name+" vừa tạo tài khoản trên ứng dụng", "Có tài khoản mới");
                    Sender content = new Sender(token.getToken(), notification);

                    mService.sendNotification(content).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success == 1) {
                                }
                                else {
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.e("Error", t.getMessage());
                        }
                    });
                    break;
                    }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
