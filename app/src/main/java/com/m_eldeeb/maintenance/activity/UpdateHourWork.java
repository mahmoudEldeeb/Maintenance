package com.m_eldeeb.maintenance.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.m_eldeeb.maintenance.MainActivity;
import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.adapter.HourWorkAdapter;
import com.m_eldeeb.maintenance.adapter.MyMachineAdapter;
import com.m_eldeeb.maintenance.helper;
import com.m_eldeeb.maintenance.interfaces.Communication;
import com.m_eldeeb.maintenance.interfaces.GetData;
import com.m_eldeeb.maintenance.models.MachineModel;
import com.m_eldeeb.maintenance.models.MachineModelResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateHourWork extends AppCompatActivity implements Communication{

    RecyclerView recyclerView;
    List<MachineModel>machineModelList=new ArrayList<>();
    HourWorkAdapter adapter;
InterstitialAd interstitial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent()!=null){
        }
        setContentView(R.layout.activity_update_hour_work);
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
        boolean hour = getBaseContext().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getBoolean("hour", false);

        if(hour){

            getUserMachine();
        }
        else { Toast.makeText(getBaseContext(), getText(R.string.can_not_now), Toast.LENGTH_SHORT).show();
        }
    }
    public void getUserMachine(){
        String userId = this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getString("id", "-1");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<MachineModelResult> connection;
        GetData registerFunction = retrofit.create(GetData.class);

        connection = registerFunction.getUserMacine(userId);
        connection.enqueue(new Callback<MachineModelResult>() {
            @Override
            public void onResponse(Call<MachineModelResult> call, Response<MachineModelResult> response) {


                try {
                    machineModelList=response.body().getUsermacine();

                    adapter = new HourWorkAdapter(UpdateHourWork.this,machineModelList);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }


            }
            @Override
            public void onFailure(Call<MachineModelResult> call, Throwable t) {

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
}
