package com.tandai.orderfood.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tandai.orderfood.R;
import com.tandai.orderfood.Model.Rating;

import java.util.ArrayList;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    ArrayList<Rating> arrRating;
    Context context;

    public RatingAdapter(ArrayList<Rating> arrRating, Context context) {
        this.arrRating = arrRating;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_rating,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Rating rating = arrRating.get(i);
        viewHolder.name.setText(rating.getName());
        viewHolder.comment.setText(rating.getComment());
        viewHolder.date.setText("("+rating.getDateTime()+")");
        viewHolder.ratingBar.setRating(Float.parseFloat(rating.getRateValue()));

    }

    @Override
    public int getItemCount() {
        return arrRating.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView name , comment,date;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_name_rating);
            comment = (TextView) itemView.findViewById(R.id.item_comment_rating);
            date = (TextView) itemView.findViewById(R.id.item_date_rating);
            ratingBar= (RatingBar) itemView.findViewById(R.id.item_star_rating);
        }
    }


}
