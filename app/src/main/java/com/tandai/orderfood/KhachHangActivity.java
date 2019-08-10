package com.tandai.orderfood;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tandai.orderfood.Adapter.FoodAdapter1;
import com.tandai.orderfood.Model.Banner;
import com.tandai.orderfood.Model.Favorite;
import com.tandai.orderfood.Model.Food;
import com.tandai.orderfood.Model.MonAn;
import com.tandai.orderfood.Model.Order;
import com.tandai.orderfood.Model.User;
import com.tandai.orderfood.Notifications.Token;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class KhachHangActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView ten, tenTK;
    ListView lvFood;
    ArrayList<Food> arrFood;
    FoodAdapter1 adapter = null;



    //Slider
    HashMap<String,String> image_list;
    SliderLayout mSlider;

    // Refresh Layout
//    SwipeRefreshLayout swipeRefreshLayout;



    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    DatabaseReference mDatabase;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    private static final int TIME_DELAY = 2500;
    private static long back_pressed = 0;



    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        updateToken(FirebaseInstanceId.getInstance().getToken());

        //set color status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

//        //Note  add this code before setcontentView
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Rubik.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build());

        setContentView(R.layout.activity_customer);


//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
//        swipeRefreshLayout.setColorSchemeResources(R.color.pDarkGreen, R.color.Orange, R.color.Red, R.color.colorBlue);
//
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if(isNetworkAvailable()){
//                    LoadData_Food();
//                }else{
//                    Toast.makeText(KhachHangActivity.this, "Vui lòng kiểm tra kết nối Internet", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 1500);
//
//
//            }
//        });


        //setup Slider
        setupSlider();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MENU");

        setSupportActionBar(toolbar);

        final CounterFab fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Cart = new Intent(KhachHangActivity.this,CartActivity.class);
                startActivity(Cart);
            }
        });

        //set count for counterFab
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Carts").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if( ds.getValue() != null) count++;
                }
                fab.setCount(count);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Init paper
        Paper.init(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        ten = (TextView) headerview.findViewById(R.id.ten);
        tenTK =( TextView) headerview.findViewById(R.id.tenTK);

        LoadData_User();

        lvFood  =   (ListView) findViewById(R.id.lvFood);
        arrFood = new ArrayList<>();
        adapter = new FoodAdapter1(this, R.layout.item_food, arrFood);
        lvFood.setAdapter(adapter);




        LoadData_Food();

        lvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //position là vi tri tren listview
                Food food = arrFood.get(position);
                Intent foodDetail = new Intent(KhachHangActivity.this,FoodDetailActivity.class);
                //gửi FoodId (ten của Food) và id quán đến activity FoodDetail
                foodDetail.putExtra("FoodId",food.getTenMon());
                foodDetail.putExtra("RestaurentID",food.getIDQuan());
                // mở activity  foodDetail
                startActivity(foodDetail);
            }
        });




    }

    private void setupSlider() {
        mSlider = (SliderLayout) findViewById(R.id.slider);
        image_list = new HashMap<>();

        final DatabaseReference banners = FirebaseDatabase.getInstance().getReference().child("Banner");

        banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Integer i = 0;
                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    //i = i+1;
                    Banner banner = postSnapShot.getValue(Banner.class);
                    image_list.put(banner.getId()+"_"+banner.getIdQuan(),banner.getImage());

                }
                for(String key:image_list.keySet()){
                    String[] keySplit = key.split("_");
                    String nameOfFood = keySplit[0];
                    String idOfRestaurent= keySplit[1];

                    //Creative Slider
                    final TextSliderView textSliderView = new TextSliderView(getBaseContext());
                    textSliderView
                            .description(nameOfFood)
                            .image(image_list.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent intent = new Intent(KhachHangActivity.this,FoodDetailActivity.class);
                                    intent.putExtras(textSliderView.getBundle());
                                    startActivity(intent);
                                }

                            });

                    //add extra bundle
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("FoodId",nameOfFood);
                    textSliderView.getBundle().putString("RestaurentID",idOfRestaurent);

                    mSlider.addSlider(textSliderView);

                    //Remove event after finish
                    banners.removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(4000);
    }


    private void LoadData_User(){
        mDatabase  = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User uInfo = dataSnapshot.getValue(User.class);
                ten.setText(uInfo.getName());
                tenTK.setText(uInfo.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(eventListener);
    }


    private  void LoadData_Food(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("QuanAn");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    int i= 0;
                    for(DataSnapshot ds1: ds.getChildren()){
                        MonAn mon = ds1.getValue(MonAn.class);
                        arrFood.add(new Food(mon.getTenMon(),mon.getTenQuan(),mon.getLinkAnh(),mon.getIdQuan(),mon.getGiaMon(),mon.getTinhTrang()));
                        adapter.notifyDataSetChanged();
                        ++i;
                        if(i==3) break; // moi quan 3 mon
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(eventListener);

    }

    // kiểm tra kết nối internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStop() {

        super.onStop();
        mSlider.stopAutoCycle();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if(back_pressed + TIME_DELAY > System.currentTimeMillis()){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(new Intent(KhachHangActivity.this,SearchFoodActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_xemthongtin) {
            Intent info = new Intent(KhachHangActivity.this,InfoPersonActivity.class);
            info.putExtra("userID",userID);//gửi UserID đến activity mới
            startActivity(info);
        }
        else if (id == R.id.nav_timkiem) {
            startActivity(new Intent(KhachHangActivity.this, SearchFoodActivity.class));
        }
        else if (id == R.id.nav_donhang) {
            //get date-time
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy   hh:mm:ss aa");
            final String currDateTime = dateFormat.format(c.getTime());


            db.child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean check = false;
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        for(DataSnapshot ds1: ds.getChildren()){
                            if(ds1.getKey().equals(userID)) {
                                for (DataSnapshot ds2 : ds1.getChildren()) {
                                    Order order = ds2.getValue(Order.class);
                                    if (getDate(order.getDateTime()) == getDate(currDateTime) ) {
                                        check = true;
                                        break;
                                    }
                                }
                            }
                            if(check) break;
                        }
                        if(check) break;
                    }
                    if (check)
                        startActivity(new Intent(KhachHangActivity.this, OrderActivity.class));
                    else
                        Toast.makeText(KhachHangActivity.this, "Chưa có đơn hàng ngày hôm nay.", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else if (id == R.id.nav_giohang) {
            startActivity(new Intent(KhachHangActivity.this, CartActivity.class));
        }
        else if(id == R.id.nav_fav){
            db.child("Favorite").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean check =false;
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        if(dataSnapshot.getValue() != null){
                            Favorite favorite = ds.getValue(Favorite.class);
                            if(favorite.getCheck() == 1){
                                check =true;
                                break;
                            }
                        }
                    }
                    if (check)
                        startActivity(new Intent(KhachHangActivity.this, FavoriteActivity.class));
                    else
                        Toast.makeText(KhachHangActivity.this, "Chưa có món ăn yêu thích", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else if (id == R.id.nav_doimk) {
            startActivity(new Intent(KhachHangActivity.this,ChangePassActivity.class));
        }
        else if(id == R.id.nav_dangxuat) {
            // open dialog
            final Dialog dialogLogOut = new Dialog(KhachHangActivity.this,R.style.Theme_Dialog);
            dialogLogOut.setContentView(R.layout.dialog_dang_xuat);
            dialogLogOut.show();

            Button khong=(Button) dialogLogOut.findViewById(R.id.btnKhongDialogDangXuat);
            Button thoat=(Button) dialogLogOut.findViewById((R.id.btnDialogDangXuat));
            khong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogLogOut.cancel();
                }
            });
            thoat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogLogOut.cancel();
                    //delete remember user and password
                    Paper.book().destroy();

                    startActivity(new Intent(KhachHangActivity.this,WelcomActivity.class));
                }
            });
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token,1);
        reference.child(user.getUid()).setValue(token1);
    }

    private Integer getDate(String date){
        return Integer.parseInt(date.substring(0,2));
    }

}
