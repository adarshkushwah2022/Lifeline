package com.example.mcproject.sendNotification;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface messagingAPIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA8hwQw3k:APA91bFPAGV4HOjd--Yf2EIj1GuhugYcMuJXjRQILoxCRlNcIxR_iHU2Lbnz73hrzQJ9KVViq-yeRcHeHfFp8tt9O3YNTzyPkpXZlrF1Nq03gIXdwUTqWteS8vVrJ80b9gCS-NprxS8A"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}

