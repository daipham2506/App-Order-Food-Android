package com.tandai.orderfood.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tandai.orderfood.Interface.ItemClickListener;
import com.tandai.orderfood.R;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView name,address;
    public CircleImageView image;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public UserViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.item_name_user);
        address = itemView.findViewById(R.id.item_address_user);
        image = itemView.findViewById(R.id.item_image_user);
        itemView.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
