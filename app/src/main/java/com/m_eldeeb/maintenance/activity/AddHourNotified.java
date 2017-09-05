package com.m_eldeeb.maintenance.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.helper;
import com.m_eldeeb.maintenance.interfaces.GetData;
import com.m_eldeeb.maintenance.models.MachineModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddHourNotified extends AppCompatActivity {

    TextView catName;
    TextView modelName;
String MacineRowId="";
    EditText workHour;
    Button addButton;
    InterstitialAd interstitial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hour_notified);
        watchAds();

        catName = (TextView) findViewById(R.id.catName);
        modelName = (TextView)findViewById(R.id.modelName);
        workHour = (EditText) findViewById(R.id.workHour2);
        addButton = (Button) findViewById(R.id.addButton2);
        if(getIntent()!=null){
            Intent i=getIntent();
            catName.setText(i.getStringExtra("catName"));
            modelName.setText(i.getStringExtra("modelName"));
            MacineRowId=i.getStringExtra("MacineRowId");
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String e=workHour.getText().toString();
                if(e.isEmpty()){ Toast.makeText(getBaseContext(), getText(R.string.enterHourWork), Toast.LENGTH_SHORT).show();
                }
                else{
                    updateHourWork(MacineRowId,e);
                }

            }
        });

    }

    public  void watchAds(){

        if(new helper(this).isOnline()) {
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId("ca-app-pub-4734245873783137/9855964837");
            AdRequest adRequest = new AdRequest.Builder().build();

            interstitial.loadAd(adRequest);
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    // Call displayInterstitial() function
                    displayInterstitial();
                }
            });

        }
        else Toast.makeText(getBaseContext(),"no internet",Toast.LENGTH_SHORT).show();

    }
    public void displayInterstitial() {

        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
    private void beginPlayingGame() {
        // Play for a while, then display the New Game Button
    }
    private void requestNewInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        interstitial.loadAd(adRequest);


    }
    public void updateHourWork(String row, String hour){
        String userId = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("id", "-1");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<ResponseBody> connection;
        GetData loginObgect = retrofit.create(GetData.class);
        connection = loginObgect.updatefullworkTime(userId,row,hour);
        connection.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String RegisterResult = null;

                try {
                    RegisterResult = response.body().string();

                    JSONObject jso = new JSONObject(RegisterResult);
                    String code= jso.getString("code");

                    if(code.equals("200")){
                        Toast.makeText(getBaseContext(), getText(R.string.done), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                    }



                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();


            }
        });

    }
}
