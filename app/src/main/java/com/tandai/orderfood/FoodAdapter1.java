package com.tandai.orderfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.nio.FloatBuffer;
import java.util.List;

public class FoodAdapter1 extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Food> ListFood;

    public FoodAdapter1(Context context, int layout, List<Food> listFood) {
        this.context = context;
        this.layout = layout;
        ListFood = listFood;
    }

    @Override
    public int getCount() {
        return ListFood.size();
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
        TextView tenMon = (TextView) view.findViewById(R.id.tvtenMon2);
        TextView tenQuan = (TextView) view.findViewById(R.id.tvtenQuan2);
        ImageView hinh = (ImageView) view.findViewById(R.id.ivHinh2);

        // set value

        Food food = ListFood.get(position);
        tenMon.setText(food.getTenMon()+" - ");
        tenQuan.setText(food.getTenQuan());
        Picasso.with(context).load(food.getLinkAnh()).into(hinh);
        return view;
    }
}
