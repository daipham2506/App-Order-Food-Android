

package com.tandai.orderfood.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tandai.orderfood.Model.Cart;
import com.tandai.orderfood.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    ArrayList<Cart> arrCart;
    Context context;

    public CartAdapter(ArrayList<Cart> arrCart, Context context) {
        this.arrCart = arrCart;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_cart,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Cart cart = arrCart.get(i);
        viewHolder.nameOfFood.setText(cart.getTenMon());
        viewHolder.price.setText(String.valueOf(cart.getGiaMon())+"Đ");
        viewHolder.nameOfRes.setText(cart.getTenQuan());
        viewHolder.quantity.setText(String.valueOf(cart.getSoluong()));
        Picasso.with(context).load(cart.getLinkAnh()).into(viewHolder.image);

    }

    @Override
    public int getItemCount() {
        return arrCart.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView nameOfFood,price,nameOfRes,quantity;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfFood = (TextView) itemView.findViewById(R.id.cart_item_name);
            price = (TextView) itemView.findViewById(R.id.cart_item_price);
            nameOfRes = (TextView) itemView.findViewById(R.id.cart_item_name_res);
            quantity = (TextView) itemView.findViewById(R.id.cart_item_count);
            image = (ImageView) itemView.findViewById(R.id.cart_item_image);
        }
    }


}

