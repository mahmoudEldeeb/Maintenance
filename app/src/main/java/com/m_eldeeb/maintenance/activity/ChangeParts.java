package com.m_eldeeb.maintenance.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.adapter.ChangePartAdapter;
import com.m_eldeeb.maintenance.adapter.HourWorkAdapter;
import com.m_eldeeb.maintenance.helper;
import com.m_eldeeb.maintenance.interfaces.Communication;
import com.m_eldeeb.maintenance.interfaces.GetData;
import com.m_eldeeb.maintenance.models.ChangeModel;
import com.m_eldeeb.maintenance.models.ChangeModelResult;
import com.m_eldeeb.maintenance.models.MachineModel;
import com.m_eldeeb.maintenance.models.MachineModelResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangeParts extends AppCompatActivity implements Communication{
    RecyclerView recyclerView;
    List<ChangeModel> machineModelList=new ArrayList<>();
    ChangePartAdapter adapter;
InterstitialAd interstitial;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_parts);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        AdView mAdView = (AdView) findViewById(R.id.adView1);
        AdView mAdView2 = (AdView) findViewById(R.id.adView2);
        if(new helper(this).isOnline()){
            mAdView.setVisibility(View.VISIBLE);

            mAdView2.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView2.loadAd(adRequest);
        }


        recyclerView = (RecyclerView) findViewById(R.id.catRes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getUserChangeMachine();
    }
    public void getUserChangeMachine(){
        progressBar.setVisibility(View.VISIBLE);
        String userId = this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getString("id", "-1");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<ChangeModelResult> connection;
        GetData getData = retrofit.create(GetData.class);

        connection = getData.userNotfications(userId);
        connection.enqueue(new Callback<ChangeModelResult>() {
            @Override
            public void onResponse(Call<ChangeModelResult> call, Response<ChangeModelResult> response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {

                        machineModelList=response.body().getNotfications();


                   if (machineModelList.size()==0){
                       Toast.makeText(getBaseContext(), getText(R.string.no_thing_need_fix), Toast.LENGTH_SHORT).show();

                   }
                   else{
                       adapter = new ChangePartAdapter(ChangeParts.this,machineModelList);
                       recyclerView.setAdapter(adapter);
                   }


                    } catch (Exception e) {
                    Toast.makeText(getBaseContext(), getText(R.string.no_thing_need_fix), Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<ChangeModelResult> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onclick(MachineModel m) {
        watchAds();
    }

    public  void watchAds(){

        if(new helper(this).isOnline()) {
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId("ca-app-pub-4734245873783137/6428033327");
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
}
