package com.m_eldeeb.maintenance.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.adapter.MyMachineAdapter;
import com.m_eldeeb.maintenance.helper;
import com.m_eldeeb.maintenance.interfaces.Communication;
import com.m_eldeeb.maintenance.interfaces.GetData;
import com.m_eldeeb.maintenance.models.MachineModel;
import com.m_eldeeb.maintenance.models.MachineModelResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mymachine extends Fragment{


    public Mymachine() {
        // Required empty public constructor
    }
RecyclerView recyclerView;
MyMachineAdapter adapter;
    List<MachineModel>machineModelList=new ArrayList<>();
    String userId;
   ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_mymachine, container, false);
        AdView mAdView = (AdView) view.findViewById(R.id.adView1);
        AdView mAdView2 = (AdView) view.findViewById(R.id.adView2);
        if(new helper(getActivity()).isOnline()){
            mAdView.setVisibility(View.VISIBLE);

            mAdView2.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView2.loadAd(adRequest);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.machineres);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userId = getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getString("id", "-1");
        progressBar= (ProgressBar) view.findViewById(R.id.progressBar);

        getUserMachine();
        return view;
    }
    public void getUserMachine(){
        progressBar.setVisibility(View.VISIBLE);
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
if(machineModelList.size()!=0){
                    adapter = new MyMachineAdapter(getActivity(),machineModelList);
                    recyclerView.setAdapter(adapter);}
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
progressBar.setVisibility(View.INVISIBLE);

            }
            @Override
            public void onFailure(Call<MachineModelResult> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }



}
