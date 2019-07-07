package com.tandai.orderfood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tandai.orderfood.Adapter.RestaurentViewOrderAdapter;
import com.tandai.orderfood.Model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import info.hoang8f.widget.FButton;

public class RestaurantViewOrderActivity extends AppCompatActivity {

    ListView listOrder;
    ArrayList<Order> arrOrder;
    RestaurentViewOrderAdapter adapter = null;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    DatabaseReference mDatabase;
    FButton btnThoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_restaurant_view_order);

        btnThoat =(FButton) findViewById(R.id.btnThoatXemdonhangQuanan);
        listOrder  =   (ListView) findViewById(R.id.listRestaurent_viewOrder);
        arrOrder = new ArrayList<>();
        adapter = new RestaurentViewOrderAdapter(this, R.layout.item_restaurent_view_order, arrOrder);
        listOrder.setAdapter(adapter);
        getInfoOrder();

        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantViewOrderActivity.this, RestaurantActivity.class));
            }
        });


        listOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = arrOrder.get(position);

                Intent OrderDetail = new Intent(RestaurantViewOrderActivity.this, DetailOrderActivity.class);
                //gửi FoodId (ten của Food) và id quán đến activity FoodDetail
                OrderDetail.putExtra("FoodID",order.getTenMon());
                OrderDetail.putExtra("CustomerID",order.getUserID());
                // mở activity  foodDetail
                startActivity(OrderDetail);
            }
        });
    }


    private void getInfoOrder(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Orders").child(userID);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get date-time
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy   hh:mm:ss aa");
                final String dateCurrent = dateFormat.format(c.getTime());

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for( DataSnapshot ds1 : ds.getChildren()){
                        if( ds1.getValue() != null){
                            Order order = ds1.getValue(Order.class);
                            int dayOrder = getDayTime(order.getDateTime());
                            int dayCurrent = getDayTime(dateCurrent);
                            if (dayCurrent == dayOrder) {
                                if (order.getCheck() == 0) {  // if not confirm then add listview
                                    arrOrder.add(order);
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                mDatabase.setValue(null);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private Integer getDayTime(String date){
        return Integer.parseInt(date.substring(0,2));
    }

}
