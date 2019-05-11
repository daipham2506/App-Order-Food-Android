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

public class RestaurentViewOrderAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Order> listOrder;

    public RestaurentViewOrderAdapter(Context context, int layout, List<Order> listOrder) {
        this.context = context;
        this.layout = layout;
        this.listOrder = listOrder;
    }


    @Override
    public int getCount() {
        return listOrder.size();
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
        TextView tenMon = (TextView) view.findViewById(R.id.res_vieworder_item_name);
        //TextView giaMon =(TextView) view.findViewById(R.id.res_vieworder_item_price);
        TextView tenKH = (TextView) view.findViewById(R.id.res_vieworder_item_name_customer);
        TextView sdt = (TextView) view.findViewById(R.id.res_vieworder_item_sdt_customer);
       // TextView diachi =(TextView) view.findViewById(R.id.res_vieworder_item_address_customer);
        ImageView hinh = (ImageView) view.findViewById(R.id.res_vieworder_item_image);
        //TextView soluong= (TextView) view.findViewById(R.id.res_vieworder_item_count);
        //TextView dateTime = (TextView) view.findViewById(R.id.res_vieworder_item_date_time);

        //set value
        Order order = listOrder.get(position);

        tenMon.setText(order.getTenMon());
        //giaMon.setText(String.valueOf(order.getGiaMon())+"đ");
        tenKH.setText((order.getTenkhachhang()));
        sdt.setText(order.getSdtKhachHang());
        //diachi.setText(order.getDiachigiaohang());
        //soluong.setText(String.valueOf(order.getSoluong()));
        //dateTime.setText(order.getDateTime());
        Picasso.with(context).load(order.getLinkAnh()).into(hinh);

        return view;
    }
}
