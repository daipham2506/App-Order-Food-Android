package com.tandai.orderfood;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tandai.orderfood.Adapter.CartAdapter;
import com.tandai.orderfood.Model.Cart;
import com.tandai.orderfood.Model.Common;
import com.tandai.orderfood.Model.Order;
import com.tandai.orderfood.Model.User;
import com.tandai.orderfood.Notifications.APIService;
import com.tandai.orderfood.Notifications.MyResponse;
import com.tandai.orderfood.Notifications.Notification;
import com.tandai.orderfood.Notifications.RetrofitClient;
import com.tandai.orderfood.Notifications.Sender;
import com.tandai.orderfood.Notifications.Token;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartActivity extends AppCompatActivity {
    ArrayList<Cart> arrCart;
    ArrayList<Order> arrOrder;
    CartAdapter adapter = null;
    TextView total;
    FButton btnDatHang;
    long tongTien = 0;
    String sdt ="",tenKH="";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    User uInfo;
    DatabaseReference mDatabase, mDatabase1;

    RecyclerView recyclerView;
    RelativeLayout relativeLayout;

    APIService mService;

    ArrayList<String> arrID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cart);

        mService = Common.getFCMService();

        total       = (TextView) findViewById(R.id.total);
        btnDatHang  = (FButton) findViewById(R.id.btnPlaceOrder);
        arrCart = new ArrayList<>();
        arrOrder = new ArrayList<>();


        initRecyclerView();


        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrCart.size() > 0) {

                    //open dialog_confirmCart
                    final Dialog dialogConfirm = new Dialog(CartActivity.this,R.style.Theme_Dialog);
                    dialogConfirm.setContentView(R.layout.dialog_confirmcart);
                    //anh xa
                    final EditText diaChi = (EditText) dialogConfirm.findViewById(R.id.diachigiaohang);
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
                                        uInfo = dataSnapshot.getValue(User.class);
                                        // get sdt + ten khach hang
                                        sdt = uInfo.getPhone();
                                        tenKH = uInfo.getName();
                                        // them vào mảng Order
                                        for (int i = 0; i < arrCart.size(); i++) {
                                            arrOrder.add(new Order(dateTime, diachigiaohang, sdt, userID,
                                                    tenKH, arrCart.get(i).getTenQuan(), arrCart.get(i).getIDQuan(),
                                                    arrCart.get(i).getTenMon(), arrCart.get(i).getGiaMon(),
                                                    arrCart.get(i).getSoluong(), arrCart.get(i).getLinkAnh(),0));
                                            if(!arrID.contains(arrCart.get(i).getIDQuan())){
                                                arrID.add(arrCart.get(i).getIDQuan());
                                            }
                                        }

                                        //Send notification
                                        sendNotification(tenKH,arrID);
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
                                Toast.makeText(CartActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();

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


                                startActivity(new Intent(CartActivity.this, KhachHangActivity.class));

                            }

                        }
                    });

                }
                else{
                    Toast.makeText(CartActivity.this, "Không có món ăn trong Giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }



        });

    }



    private void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_cart);
        relativeLayout = (RelativeLayout) findViewById(R.id.layoutCart);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        mDatabase = FirebaseDatabase.getInstance().getReference().child("Carts").child(userID);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Cart cart = ds.getValue(Cart.class);
                    arrCart.add(cart);
                    tongTien = tongTien + cart.getTongTien();
                }
                total.setText(String.valueOf(tongTien)+ "đ");

                final CartAdapter cartAdapter = new CartAdapter(arrCart,getApplicationContext());
                cartAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(cartAdapter);


                // set animation
                Common.runAnimation(recyclerView);



                //swipe to delete food or UNDO
                ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

                    Drawable background;
                    Drawable xMark;
                    int xMarkMargin;
                    boolean initiated;

                    private void init() {
                        background = new ColorDrawable(Color.RED);
                        xMark = ContextCompat.getDrawable(CartActivity.this, R.drawable.ic_delete_white_36);
                        xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        initiated = true;
                    }

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        Toast.makeText(CartActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                        return false;
                    }


                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        //Remove swiped item from list and notify the RecyclerView
                        final int position = viewHolder.getAdapterPosition();

                        final Cart mRecentlyDeletedItem = arrCart.get(position);
                        final int mRecentlyDeletedItemPosition = position;
                        arrCart.remove(position);
                        cartAdapter.notifyDataSetChanged();
                        tongTien = tongTien - mRecentlyDeletedItem.getTongTien();
                        total.setText(String.valueOf(tongTien)+ "đ");


                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, "Đã xóa "+mRecentlyDeletedItem.getTenMon(), Snackbar.LENGTH_LONG)
                                .setAction("Quay lại", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Snackbar snackbar1 = Snackbar.make(relativeLayout, "Đã khôi phục "+mRecentlyDeletedItem.getTenMon(), Snackbar.LENGTH_LONG);
                                        arrCart.add(mRecentlyDeletedItemPosition,mRecentlyDeletedItem);
                                        cartAdapter.notifyDataSetChanged();
                                        tongTien = tongTien + mRecentlyDeletedItem.getTongTien();
                                        total.setText(String.valueOf(tongTien)+ "đ");
                                        snackbar1.show();
                                    }
                                });

                        snackbar.show();

                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        View itemView = viewHolder.itemView;

                        // not sure why, but this method get's called for viewholder that are already swiped away
                        if (viewHolder.getAdapterPosition() == -1) {
                            // not interested in those
                            return;
                        }

                        if (!initiated) {
                            init();
                        }

                        // draw red background
                        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        background.draw(c);

                        int xMarkLeft = 0;
                        int xMarkRight = 0;
                        int xMarkTop = itemView.getTop() + (itemView.getHeight() - xMark.getIntrinsicHeight()) / 2;
                        int xMarkBottom = xMarkTop + xMark.getIntrinsicHeight();
                        if (dX < 0) {
                            xMarkLeft = itemView.getRight() - xMarkMargin - xMark.getIntrinsicWidth();
                            xMarkRight = itemView.getRight() - xMarkMargin;
                        }
                        else {
                            xMarkLeft = itemView.getLeft() + xMarkMargin;
                            xMarkRight = itemView.getLeft() + xMarkMargin + xMark.getIntrinsicWidth();
                        }
                        xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                        xMark.draw(c);

                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }

                };

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendNotification(final String nameCustomer, final ArrayList<String> arrId){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = tokens.orderByChild("checkToken").equalTo(2); // get all node isServerToken is 2
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Token serverToken = ds.getValue(Token.class);
                    for(int i=0;i<arrId.size();i++){
                        if(arrId.get(i).equals(ds.getKey())) {
                            Notification notification = new Notification(nameCustomer + " vừa đặt món ăn từ quán của bạn","Có đơn hàng mới");
                            Sender content = new Sender(serverToken.getToken(), notification);

                            mService.sendNotification(content).enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            //Toast.makeText(CartActivity.this, "thành công", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //Toast.makeText(CartActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("Error", t.getMessage());
                                }
                            });
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}