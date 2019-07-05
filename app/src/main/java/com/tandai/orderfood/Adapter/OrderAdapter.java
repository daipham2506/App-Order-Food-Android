package com.tandai.orderfood.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tandai.orderfood.Model.Order;
import com.tandai.orderfood.R;

import java.util.ArrayList;

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
