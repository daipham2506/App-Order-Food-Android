package com.tandai.orderfood;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    ArrayList<Order> arrOrder;
    Context context;


    public OrderAdapter(ArrayList<Order> arrOrder, Context context) {
        this.arrOrder = arrOrder;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_order,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Order order = arrOrder.get(i);
        viewHolder.name.setText(order.getTenMon());
        viewHolder.price.setText(String.valueOf(order.getGiaMon())+"Đ");
        viewHolder.quantity.setText(" - "+order.getSoluong());
        Picasso.with(context).load(order.getLinkAnh()).into(viewHolder.image);
        if(order.getCheck() == 0){
            viewHolder.status.setText("Chưa xác nhận");
            viewHolder.status.setTextColor(Color.GRAY);
        }
        else if(order.getCheck() == 1){
            viewHolder.status.setText("Đã giao");
            viewHolder.status.setTextColor(Color.parseColor("#00C853"));
        }
        else if(order.getCheck() == 2){
            viewHolder.status.setText("Đang giao");
            viewHolder.status.setTextColor(Color.BLUE);
        }
        else if(order.getCheck() == 3){
            viewHolder.status.setText("Hết hàng");
            viewHolder.status.setTextColor(Color.RED);
        }


    }





    @Override
    public int getItemCount() {
        return arrOrder.size();
    }



    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView name , price,quantity,status;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_order_name);
            price = (TextView) itemView.findViewById(R.id.item_order_price);
            image = (ImageView) itemView.findViewById(R.id.item_order_image);
            quantity = (TextView) itemView.findViewById(R.id.item_order_quantity);
            status = (TextView) itemView.findViewById(R.id.item_order_status);
        }
    }




}
