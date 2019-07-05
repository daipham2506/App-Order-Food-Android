package com.tandai.orderfood.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tandai.orderfood.Model.Cart;
import com.tandai.orderfood.Model.Favorite;
import com.tandai.orderfood.Model.MonAn;
import com.tandai.orderfood.R;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    ArrayList<Favorite> arrFavorite;
    Context context;

    DatabaseReference databaseReference;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();



    public FavoriteAdapter(ArrayList<Favorite> arrFavorite, Context context) {
        this.arrFavorite = arrFavorite;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_favorite,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Favorite favorite = arrFavorite.get(i);
        viewHolder.name.setText(favorite.getFoodID());
        viewHolder.price.setText(String.valueOf(favorite.getPrice())+"Đ");
        Picasso.with(context).load(favorite.getImage()).into(viewHolder.image);

        viewHolder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference = FirebaseDatabase.getInstance().getReference().child("QuanAn").child(favorite.getRestaurentID()).child(favorite.getFoodID());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() !=null){
                            MonAn food = dataSnapshot.getValue(MonAn.class);
                            if(food.getTinhTrang() == 1) {
                                Cart cart = new Cart(food.getTenMon(),food.getTenQuan(),food.getIdQuan(),food.getLinkAnh(),food.getGiaMon(),1,food.getGiaMon());
                                //them vao database
                                mDatabase.child("Carts").child(userID).child(food.getTenMon()).setValue(cart);
                                Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(context, "Món ăn đã hết", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


    }





    @Override
    public int getItemCount() {
        return arrFavorite.size();
    }



    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView name , price;
        ImageView image, cart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_fav_name);
            price = (TextView) itemView.findViewById(R.id.item_fav_price);
            image = (ImageView) itemView.findViewById(R.id.item_fav_image);
            cart= (ImageView) itemView.findViewById(R.id.item_fav_cart);
        }
    }




}
