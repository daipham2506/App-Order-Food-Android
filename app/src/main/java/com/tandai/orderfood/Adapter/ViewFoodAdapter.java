package com.tandai.orderfood.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
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
import com.tandai.orderfood.Model.MonAn;
import com.tandai.orderfood.R;

import java.util.ArrayList;

public class ViewFoodAdapter extends RecyclerView.Adapter<ViewFoodAdapter.ViewHolder> {
    ArrayList<MonAn> arrMonAn;
    Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mDatabase;


    public ViewFoodAdapter(ArrayList<MonAn> arrMonAn, Context context) {
        this.arrMonAn = arrMonAn;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_view_food,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final MonAn monAn = arrMonAn.get(i);
        viewHolder.name.setText(monAn.getTenMon());
        viewHolder.price.setText(monAn.getGiaMon()+"Đ");
        if(monAn.getTinhTrang() == 0){
            viewHolder.status.setText("Hết hàng");
            viewHolder.status.setTextColor(Color.GRAY);
        }
        else {
            viewHolder.status.setText("Còn hàng");
        }
        Picasso.with(context).load(monAn.getLinkAnh()).into(viewHolder.image);


    }


    @Override
    public int getItemCount() {
        return arrMonAn.size();
    }



    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView name , price,status;
        ImageView image;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_food_name_view);
            price = (TextView) itemView.findViewById(R.id.item_food_price_view);
            image = (ImageView) itemView.findViewById(R.id.item_food_image_view);
            status = (TextView) itemView.findViewById(R.id.item_food_status_view);
            cardView = (CardView) itemView.findViewById(R.id.cardView_food);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Lựa chọn Option");
            menu.add(this.getAdapterPosition(),121,0,"Xóa món ăn");
            menu.add(this.getAdapterPosition(),122,1,"Cập nhật món ăn");
            menu.add(this.getAdapterPosition(),123,2,"Đặt làm Hot Food");
        }
    }

    public void removeItem(int position, String name){
        arrMonAn.remove(position);
        notifyDataSetChanged();
        // remove in database
        mDatabase = FirebaseDatabase.getInstance().getReference("QuanAn").child(user.getUid()).child(name);
        mDatabase.setValue(null);
    }

}
