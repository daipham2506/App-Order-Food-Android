package com.tandai.orderfood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import info.hoang8f.widget.FButton;

public class CartActivity extends AppCompatActivity {
    ListView lvCart;
    ArrayList<Cart> arrCart;
    ArrayList<Order> arrOrder;
    CartAdapter adapter = null;
    TextView total;
    FButton btnDatHang;
    long tongTien = 0;
    String sdt ="",tenKH="";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    DatabaseReference mDatabase, mDatabase1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cart);

        total =(TextView) findViewById(R.id.total);
        btnDatHang =(FButton) findViewById(R.id.btnPlaceOrder);

        lvCart  =   (ListView) findViewById(R.id.listCart);
        arrCart = new ArrayList<>();
        arrOrder = new ArrayList<>();
        adapter = new CartAdapter(this, R.layout.line_cart, arrCart);
        lvCart.setAdapter(adapter);

        getDataCart();


        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open dialog_confirmCart
                final Dialog dialogConfirm = new Dialog(CartActivity.this);
                dialogConfirm.setContentView(R.layout.dialog_confirmcart);
                //anh xa
                final EditText diaChi = (EditText) dialogConfirm.findViewById(R.id.diachigiaohang) ;
                TextView huy = (TextView) dialogConfirm.findViewById(R.id.cancelCart);
                TextView xacnhan = (TextView) dialogConfirm.findViewById(R.id.confirmCart);
                dialogConfirm.show();
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogConfirm.dismiss();
                    }
                });

                xacnhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get date-time
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy   hh:mm:ss aa");
                        final String dateTime = dateFormat.format(c.getTime());

                        // get address
                        final String diachigiaohang = diaChi.getText().toString().trim();
                        if (diachigiaohang.isEmpty())
                            Toast.makeText(CartActivity.this, "Vui lòng nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                        else {
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User uInfo = dataSnapshot.getValue(User.class);
                                    // get sdt + ten khach hang
                                    sdt = uInfo.getPhone();
                                    tenKH = uInfo.getName();
                                    // them vào mảng Order
                                    for (int i = 0; i < arrCart.size(); i++) {
                                        arrOrder.add(new Order(dateTime, diachigiaohang, sdt, userID, tenKH, arrCart.get(i).getTenQuan(), arrCart.get(i).getIDQuan(), arrCart.get(i).getTenMon(), arrCart.get(i).getGiaMon(), arrCart.get(i).getSoluong(), arrCart.get(i).getLinkAnh()));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            mDatabase.addListenerForSingleValueEvent(eventListener);


                            // ghi data vào db Orders
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Orders");
                            ValueEventListener eventListener1 = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (int i = 0; i < arrOrder.size(); i++) {
                                        mDatabase.child(arrOrder.get(i).getQuanID()).child(userID).child(arrOrder.get(i).getTenMon()).setValue(arrOrder.get(i));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            mDatabase.addListenerForSingleValueEvent(eventListener1);


                            // đóng dialog
                            dialogConfirm.dismiss();
                            Toast.makeText(CartActivity.this, "Bạn đã đặt hàng thành công", Toast.LENGTH_SHORT).show();

                            // xóa db Cart của user sau khi đặt hàng thành công
                            mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Carts").child(userID);
                            ValueEventListener eventListener2 = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().setValue(null);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            mDatabase1.addListenerForSingleValueEvent(eventListener2);


                        }

                    }
                });

            }
        });

    }

    private void getDataCart(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Carts").child(userID);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Cart cart = ds.getValue(Cart.class);
                    arrCart.add(cart);
                    tongTien = tongTien + cart.getTongTien();
                    adapter.notifyDataSetChanged();
                }
                total.setText(String.valueOf(tongTien)+ "đ");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener);
    }


}
