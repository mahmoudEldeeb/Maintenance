package com.m_eldeeb.maintenance.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.google.firebase.iid.FirebaseInstanceId;
import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.helper;
import com.m_eldeeb.maintenance.interfaces.GetData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FCMRegistrationService extends IntentService {


    public FCMRegistrationService() {
        super("FCM");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(new helper(getBaseContext()).isOnline()){
        String token = FirebaseInstanceId.getInstance().getToken();

       sendTokenToServer(token);}
        else {
            Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

        }

    }


    private void sendTokenToServer(final String token) {
        String id = getBaseContext().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getString("id", "-1");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<ResponseBody> connection;
        GetData getData = retrofit.create(GetData.class);

        connection = getData.updateToken(id, token);
        connection.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();


            }
        });
    }
}
