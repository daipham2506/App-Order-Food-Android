package com.tandai.orderfood.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.tandai.orderfood.Notifications.APIService;
import com.tandai.orderfood.Notifications.RetrofitClient;
import com.tandai.orderfood.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.support.v4.content.ContextCompat.getSystemService;

public class Common {

    private static final String FCM_URL = "https://fcm.googleapis.com/";
    public static APIService getFCMService(){
        return RetrofitClient.getClient(FCM_URL).create(APIService.class);
    }


    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static void runAnimation(RecyclerView recyclerView) {
        LayoutAnimationController controller = null;

        controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_slide_from_left);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}
