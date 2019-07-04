package com.tandai.orderfood.Notifications;

import com.tandai.orderfood.Notifications.Sender;
import com.tandai.orderfood.Notifications.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAnROuZFU:APA91bExCKkmHqE4fwJQ8khpbpMtXifgZQ1HgNyfMYC0eRd-Bv5itfpCbpfN2AJ_Wkp-XibYlqzxHNj1ZUqxvhmKMhiTXnfTProA26WTdbbb2jMATzRPs3_zp_W2UyP2s9cYDv-C4BTO"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
