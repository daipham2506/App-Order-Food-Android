package com.tandai.orderfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Cart> listCart;

    public CartAdapter(Context context, int layout, List<Cart> listCart) {
        this.context = context;
        this.layout = layout;
        this.listCart = listCart;
    }

    @Override
    public int getCount() {
        return listCart.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);
        //anh xa view

        TextView tenMon = (TextView) view.findViewById(R.id.cart_item_name);
        TextView giaMon = (TextView) view.findViewById(R.id.cart_item_price);
        TextView tenQuan = (TextView) view.findViewById(R.id.cart_item_name_res);
        TextView soluong = (TextView) view.findViewById(R.id.cart_item_count);
        ImageView image = (ImageView) view.findViewById(R.id.cart_item_image);

        //set value

        Cart cart = listCart.get(position);
        tenMon.setText(cart.getTenMon());
        giaMon.setText(cart.getGiaMon()+" đ");
        tenQuan.setText("Quán "+cart.getTenQuan());
        soluong.setText(String.valueOf(cart.getSoluong()));
        Picasso.with(context).load(cart.getLinkAnh()).into(image);

        return view;
    }
}
