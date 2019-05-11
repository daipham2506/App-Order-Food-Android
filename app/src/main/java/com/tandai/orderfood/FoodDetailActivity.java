package com.tandai.orderfood;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class FoodDetailActivity extends AppCompatActivity implements RatingDialogListener {
    User quanAn;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String name;
    String foodId = "";
    String RestaurentID = "";
    FloatingActionButton btnCart,btnRating;
    ElegantNumberButton number;
    ImageView hinh;
    TextView tenmon,giamon,mota;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference database;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    MonAn food;
    long quantity = 1 , price;

    RatingBar ratingBar;


    ArrayList<Rating> arrRating;
    RatingAdapter adapter = null;
    ListView listView;

    DatabaseReference ratingTbl = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_food_detail);


        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);
        number = (ElegantNumberButton) findViewById(R.id.number_button);
        hinh = (ImageView) findViewById(R.id.image_food);
        tenmon= (TextView) findViewById(R.id.food_name);
        giamon= (TextView) findViewById(R.id.food_price);
        mota =(TextView) findViewById(R.id.food_description);
        collapsingToolbarLayout =(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        btnRating = (FloatingActionButton) findViewById(R.id.btnRating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //Nhận THông tin Food từ Intent gửi đến
        Intent intent = getIntent();
        if(intent != null){
            foodId       = intent.getStringExtra("FoodId");
            RestaurentID = intent.getStringExtra("RestaurentID");
        }
        if(!foodId.isEmpty() && foodId !=null && !RestaurentID.isEmpty() && RestaurentID!= null){
            getDetailFood(RestaurentID,foodId);
            getRatingFood(foodId);
            initRecyclerView(foodId,RestaurentID);
        }


        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(food.getTinhTrang() == 1) {
                    Cart cart = new Cart(food.getTenMon(),food.getTenQuan(),food.getIdQuan(),food.getLinkAnh(),food.getGiaMon(),quantity,price);
                    //them vao database
                    mDatabase.child("Carts").child(userID).child(food.getTenMon()).setValue(cart);
                    Toast.makeText(FoodDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(FoodDetailActivity.this, "Món ăn đã hết", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRatingFood(final String foodId) {

        //Query foodRating = ratingTbl.child("Rating").orderByChild("foodID").equalTo(foodId);

        ratingTbl.child("Rating").child(foodId).addValueEventListener(new ValueEventListener() {
            float count = 0,sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Rating item = ds.getValue(Rating.class);
                    if(item.getFoodID().equals(foodId)) {
                        sum += Float.parseFloat(item.getRateValue());
                        count++;
                    }
                }
                if( count != 0) {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Gửi")
                .setNegativeButtonText("Hủy")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Quite OK","Very Good","Exellent"))
                .setDefaultRating(3)
                .setTitle("Đánh giá món ăn")
                .setDescription("Chọn mức độ hài lòng của bạn về món ăn này")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Viết cảm nhận của bạn tại đây...")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(R.color.colorWhite)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetailActivity.this)
                .show();

    }


    //Hiển thông thông tin chi tiết món
    private void getDetailFood(String restaurentID, String foodId) {
        // get data quan
        mDatabase.child("Users").child(restaurentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quanAn = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        //set data
        mDatabase.child("QuanAn").child(restaurentID).child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // đối tượng food lấy dữ liệu từ database
                food = dataSnapshot.getValue(MonAn.class);
                //THiết lập ảnh
                Picasso.with(getBaseContext()).load(food.getLinkAnh()).into(hinh);
                collapsingToolbarLayout.setTitle(food.getTenMon());

                price = food.getGiaMon();
                giamon.setText(price +" đ");
                number.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quantity = Long.parseLong(number.getNumber());
                        price = quantity * food.getGiaMon();
                        giamon.setText(price+" đ");
                    }
                });

                tenmon.setText(food.getTenMon());
                if (food.getTinhTrang() == 1) {
                    mota.setText("Tình trạng món ăn: Còn\nQuán "+food.getTenQuan()+"\nĐịa chỉ: "+quanAn.getAddress()+"\nLiên hệ: "+quanAn.getPhone());
                } else {
                    mota.setText("Tình trạng món ăn: Hết\nQuán "+food.getTenQuan()+"\nĐịa chỉ: "+quanAn.getAddress()+"\nLiên hệ: "+quanAn.getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(final int value, @NotNull final String comment) {

        //get date-time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy  hh:mm aa");
        final String dateTime = dateFormat.format(c.getTime());
        //get name
        mDatabase.child("Users").child(userID).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
                //get Rating and upload firebase
                final Rating rating = new Rating(name,RestaurentID,foodId,String.valueOf(value),comment,dateTime);
                ratingTbl.child("Rating").child(foodId).child(userID).setValue(rating);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(FoodDetailActivity.this, "Cảm ơn bạn đã đánh giá món ăn này", Toast.LENGTH_SHORT).show();

        finish();
        startActivity(getIntent());
    }




    private void initRecyclerView(final String foodID,final String RestaurentID){
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRating);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        final ArrayList<Rating> arrRating = new ArrayList<>();


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        database = FirebaseDatabase.getInstance().getReference().child("Rating").child(foodID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Rating rating = ds.getValue(Rating.class);
                    if(rating.getFoodID().equals(foodID) && rating.getRestaurentID().equals(RestaurentID))
                        arrRating.add(rating);
                }
                RatingAdapter ratingAdapter = new RatingAdapter(arrRating,getApplicationContext());
                recyclerView.setAdapter(ratingAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


}


