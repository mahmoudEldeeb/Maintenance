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
import com.google.android.gms.ads.InterstitialAd;
import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.adapter.MyMachineAdapter;
import com.m_eldeeb.maintenance.adapter.PartAdapter;
import com.m_eldeeb.maintenance.helper;
import com.m_eldeeb.maintenance.interfaces.Communication;
import com.m_eldeeb.maintenance.interfaces.GetData;
import com.m_eldeeb.maintenance.models.MachineModel;
import com.m_eldeeb.maintenance.models.PartModel;
import com.m_eldeeb.maintenance.models.PartModelResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Parts extends AppCompatActivity implements Communication{
RecyclerView recyclerView;
    String userId;
    MachineModel model;
    List<PartModel>partModelList=new ArrayList<>();

    InterstitialAd interstitial;
    int close=0;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts);
        recyclerView = (RecyclerView) findViewById(R.id.partRes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userId = this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getString("id", "-1");


        progressBar= (ProgressBar) findViewById(R.id.progressBar5);

model=new MachineModel();

        model = (MachineModel) getIntent().getSerializableExtra("machine");

        getUserMachineParts();
    }

    @Override
    public void onBackPressed()
    {
        if (close==0){
            watchAds();
            close++;
        }
        // code here to show dialog
        else  super.onBackPressed();  // optional depending on your needs
    }
    public void getUserMachineParts(){
progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<PartModelResult> connection;
        GetData registerFunction = retrofit.create(GetData.class);
        connection = registerFunction.userSpearParts(userId,model.getCatId(),model.getModelId());
        connection.enqueue(new Callback<PartModelResult>() {
            @Override
            public void onResponse(Call<PartModelResult> call, Response<PartModelResult> response) {


                try {
                    partModelList=response.body().getSpearparts();
                  PartAdapter adapter = new PartAdapter(Parts.this,partModelList);
                    recyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<PartModelResult> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getBaseContext(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public  void watchAds(){

        if(new helper(this).isOnline()) {
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId("ca-app-pub-4734245873783137/9211955243");
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


    @Override
    public void onclick(MachineModel m) {

    }
}
