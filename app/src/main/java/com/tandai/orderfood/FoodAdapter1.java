package com.tandai.orderfood;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class FoodAdapter1 extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Food> ListFood;

//
//    public FoodAdapter1(ArrayList<Food> arrFood, Context context) {
//        this.arrFood = arrFood;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
//        View itemView = layoutInflater.inflate(R.layout.line_food,viewGroup,false);
//
//        return new FoodAdapter1.ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//        Food food = arrFood.get(i);
//        viewHolder.tenMon.setText(food.getTenMon()+" - ");
//        viewHolder.tenQuan.setText(food.getTenQuan());
//        Picasso.with(context).load(food.getLinkAnh()).into(viewHolder.hinh);
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrFood.size();
//    }
//
//    public class ViewHolder extends  RecyclerView.ViewHolder{
//        TextView tenMon,tenQuan;
//        ImageView hinh;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tenMon = (TextView) itemView.findViewById(R.id.tvtenMon2);
//            tenQuan = (TextView) itemView.findViewById(R.id.tvtenQuan2);
//            hinh = (ImageView) itemView.findViewById(R.id.ivHinh2);
//
//
//        }
//    }


    TextView tenMon,tenQuan;
    ImageView hinh,fav;
    Favorite favor;
    int check = 0;

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

        return view;
    }
}
