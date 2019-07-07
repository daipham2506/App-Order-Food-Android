package com.tandai.orderfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tandai.orderfood.Model.Order;
import com.tandai.orderfood.R;

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
        TextView tenKH = (TextView) view.findViewById(R.id.res_vieworder_item_name_customer);
        TextView sdt = (TextView) view.findViewById(R.id.res_vieworder_item_sdt_customer);
        ImageView hinh = (ImageView) view.findViewById(R.id.res_vieworder_item_image);


        //set value
        Order order = listOrder.get(position);
        tenMon.setText(order.getTenMon());
        tenKH.setText((order.getTenkhachhang()));
        sdt.setText(order.getSdtKhachHang());
        Picasso.with(context).load(order.getLinkAnh()).into(hinh);

        return view;
    }
}
