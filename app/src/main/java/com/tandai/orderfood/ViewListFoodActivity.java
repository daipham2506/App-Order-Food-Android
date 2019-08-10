package com.tandai.orderfood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tandai.orderfood.Adapter.ViewFoodAdapter;
import com.tandai.orderfood.Model.Banner;
import com.tandai.orderfood.Model.Common;
import com.tandai.orderfood.Model.MonAn;

import java.util.ArrayList;

public class ViewListFoodActivity extends AppCompatActivity {
    ImageView back;
    TextView tenquan;
    RecyclerView recyclerViewFood;
    ArrayList<MonAn> arrFood = new ArrayList<>();
    ViewFoodAdapter viewFoodAdapter;
    MonAn food;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_view_food);
        AnhXa();
        initRecyclerView();
        tenquan.setText("Quán "+ user.getDisplayName());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewListFoodActivity.this, RestaurantActivity.class));
            }
        });
    }

    private void AnhXa(){
        back = (ImageView) findViewById(R.id.btnback);
        tenquan =(TextView) findViewById(R.id.tvtenQuanLayoutXemDSMon);
    }




    private void initRecyclerView(){
        recyclerViewFood = (RecyclerView) findViewById(R.id.recycler_view_food);

        recyclerViewFood.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewFood.setLayoutManager(layoutManager);
        recyclerViewFood.setItemAnimator(new DefaultItemAnimator());

        mDatabase = FirebaseDatabase.getInstance().getReference("QuanAn").child(userID);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(dataSnapshot.getValue() != null) {
                        food = ds.getValue(MonAn.class);
                        arrFood.add(food);
                    }
                }
                viewFoodAdapter = new ViewFoodAdapter(arrFood,getApplicationContext());
                recyclerViewFood.setAdapter(viewFoodAdapter);
                //set animation
                Common.runAnimation(recyclerViewFood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int pos = item.getGroupId();
        final String name = arrFood.get(pos).getTenMon();
        switch (item.getItemId()){
            case 121:
                new AlertDialog.Builder(this)
                        .setTitle("Xóa món ăn")
                        .setMessage("Bạn muốn xóa "+name+" ?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                displayMessage("Đã xóa thành công " + name);
                                viewFoodAdapter.removeItem(pos,name);
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Hủy", null)
                        .setIcon(R.drawable.ic_delete_red_24dp)
                        .show();
                return true;
            case 122:
                DialogUpdate(name);
                return true;
            case 123:
                Banner banner = new Banner(name,userID,arrFood.get(pos).getLinkAnh());
                mDatabase = FirebaseDatabase.getInstance().getReference("Banner").child(userID);
                mDatabase.setValue(banner);
                displayMessage("Đã đặt "+ name+" làm Hot Food");
                return true;

                default:
                    return super.onContextItemSelected(item);

        }


    }

    private void displayMessage(String msg){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.view_food), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void DialogUpdate(String name){
        final Dialog dialog   = new Dialog(ViewListFoodActivity.this,R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_update_food);
        dialog.show();
        final EditText nameFood = (EditText) dialog.findViewById(R.id.updateNameFood);
        final EditText priceFood = (EditText) dialog.findViewById(R.id.updatePriceFood);
        final RadioButton conhang = (RadioButton) dialog.findViewById(R.id.conhang);
        final RadioButton hethang = (RadioButton) dialog.findViewById(R.id.hethang);
        Button update = (Button) dialog.findViewById(R.id.btnUpdateFood);
        Button cancel = (Button) dialog.findViewById(R.id.btnCancelFood);

        mDatabase = FirebaseDatabase.getInstance().getReference("QuanAn").child(user.getUid()).child(name);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MonAn food = dataSnapshot.getValue(MonAn.class);
                nameFood.setText(food.getTenMon());
                priceFood.setText(food.getGiaMon()+"");
                if(food.getTinhTrang() == 1){
                    conhang.setChecked(true);
                }
                else{
                    hethang.setChecked(true);
                }
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
                final String Name = nameFood.getText().toString();
                final String Price = priceFood.getText().toString();
                if(Name.isEmpty() || Price.isEmpty() ){
                    Toast.makeText(ViewListFoodActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else{
                    dialog.cancel();
                    mDatabase.child("tenMon").setValue(Name);
                    mDatabase.child("giaMon").setValue(Long.parseLong(Price));
                    if(conhang.isChecked()) mDatabase.child("tinhTrang").setValue(1);
                    else if(hethang.isChecked()) mDatabase.child("tinhTrang").setValue(0);
                    // Reload Activity
                    finish();
                    startActivity(getIntent());
                }
            }
        });
    }
}



