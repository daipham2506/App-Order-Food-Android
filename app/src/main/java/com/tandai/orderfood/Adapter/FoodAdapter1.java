package com.tandai.orderfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tandai.orderfood.Model.Favorite;
import com.tandai.orderfood.Model.Food;
import com.tandai.orderfood.R;

import java.util.List;

public class FoodAdapter1 extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Food> ListFood;


    TextView tenMon,tenQuan;
    ImageView hinh;

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
        tenMon = (TextView) view.findViewById(R.id.tvtenMon2);
        tenQuan = (TextView) view.findViewById(R.id.tvtenQuan2);
        hinh = (ImageView) view.findViewById(R.id.ivHinh2);

        // set value
        final Food food = ListFood.get(position);

        tenMon.setText(food.getTenMon()+" - ");
        tenQuan.setText(food.getTenQuan());
        Picasso.with(context).load(food.getLinkAnh()).into(hinh);

        // set anim
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.item_animation_from_left);

        animation.setDuration(1500);
        view.startAnimation(animation);

        return view;
    }
}
