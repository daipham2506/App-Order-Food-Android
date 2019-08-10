package com.tandai.orderfood;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tandai.orderfood.Adapter.FavoriteAdapter;
import com.tandai.orderfood.Adapter.UserViewHolder;
import com.tandai.orderfood.Interface.ItemClickListener;
import com.tandai.orderfood.Model.Common;
import com.tandai.orderfood.Model.Favorite;
import com.tandai.orderfood.Model.User;

import java.util.ArrayList;

public class ViewUserActivity extends AppCompatActivity {

    DatabaseReference database;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<User, UserViewHolder> adapter;
    ArrayList<User> arrUser = new ArrayList<>();
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_view_user);
        //set color status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        recyclerView = findViewById(R.id.recycler_view_user);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference("Users");
        home = findViewById(R.id.home_view_user);

        loadListUser();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewUserActivity.this,AdminActivity.class));
            }
        });
    }


    //Hiển thị danh sách user
    private void loadListUser() {
        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(User.class,
                R.layout.item_user,
                UserViewHolder.class,
                database)
        {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
                viewHolder.name.setText(model.getName());
                viewHolder.address.setText( model.getAddress());
                if (model.getImage() != null) {
                    Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.image);
                }

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent info = new Intent(ViewUserActivity.this,InfoPersonActivity.class);
                        info.putExtra("userID",adapter.getRef(position).getKey());//gửi UserID đến activity mới
                        startActivity(info);
                    }
                });

            }
        };
        // Thiết lập adapter
        recyclerView.setAdapter(adapter);

        Common.runAnimation(recyclerView);

    }


//    private void initRecyclerView(){
//        recyclerView = findViewById(R.id.recycler_view_user);
//
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//
//
//        database = FirebaseDatabase.getInstance().getReference("Users");
//        database.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    user = ds.getValue(User.class);
//                    if(!user.getUserType().equals("admin")) arrUser.add(user);
//                }
//                viewUserAdapter = new ViewUserAdapter(arrUser,getApplicationContext());
//                recyclerView.setAdapter(viewUserAdapter);
//                //set animation
//                Common.runAnimation(recyclerView);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
}
