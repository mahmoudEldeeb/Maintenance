package com.m_eldeeb.maintenance.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.m_eldeeb.maintenance.MainActivity;
import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.activity.Register;
import com.m_eldeeb.maintenance.helper;
import com.m_eldeeb.maintenance.interfaces.GetData;
import com.m_eldeeb.maintenance.models.categoryModel;
import com.m_eldeeb.maintenance.models.categoryModelResult;
import com.m_eldeeb.maintenance.models.model;
import com.m_eldeeb.maintenance.models.modelResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddItem extends Fragment  {


    public AddItem() {
        // Required empty public constructor
    }
Button but;
TextView tex;
    RadioGroup radiogroub;
    EditText workHour;
    ArrayAdapter<String> adapter,modeladapter;
    String[] title;
    String spinner_item;
    ProgressBar progressBar3;
    Spinner spinner_cat,spinner_model,spinnerTime;
List<categoryModel>categoryList=new ArrayList<>();
    List<String>categoryListString=new ArrayList<>();

    List<model>modelList=new ArrayList<>();
    List<String>modelListString=new ArrayList<>();
    String userId,catId,modelId,groupId,workTime;
   String  state="5";
    Button end;
    EditText date1;
    Calendar myCalendar;
    private FragmentActivity fragmentActivity;
   // SpinnerAdapter adapter;

    String myFormat = "dd-MM-yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_add_item, container, false);
        AdView mAdView = (AdView) view.findViewById(R.id.adView1);
        AdView mAdView2 = (AdView) view.findViewById(R.id.adView2);
date1= (EditText) view.findViewById(R.id.date1);
        if(new helper(getActivity()).isOnline()){
            mAdView.setVisibility(View.VISIBLE);

            mAdView2.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView2.loadAd(adRequest);
        }

end= (Button) view.findViewById(R.id.end);
        spinner_cat = (Spinner) view.findViewById(R.id.spinner_cat);
        spinner_model = (Spinner) view.findViewById(R.id.spinner_model);
        spinnerTime=(Spinner) view.findViewById(R.id.spinnerTime);
        workHour= (EditText) view.findViewById(R.id.workHour);
        but= (Button) view.findViewById(R.id.but);
        progressBar3= (ProgressBar) view.findViewById(R.id.progressBar3);


        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor =getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit();

                editor.putBoolean("finish", true);
                editor.commit();
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();


                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                UpdateWorkedHoure fragment = new UpdateWorkedHoure();
                ft.replace(R.id.fragment, fragment);

                ft.commit();

            }
        });
        getCategory();
        radiogroub= (RadioGroup) view.findViewById(R.id.radiogroub);


        final LinearLayout used1= (LinearLayout) view.findViewById(R.id.used1);
        userId = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("id", "-1");
        radiogroub.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
if(checkedId==R.id.new1){
    state="0";

    used1.setVisibility(View.GONE);
}
                else{
    state="1";
    used1.setVisibility(View.VISIBLE);
                    }

            }
        });

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("0")){
                    workTime="0";}
                else{workTime=workHour.getText().toString();}
                if(workTime.isEmpty()||spinner_item.isEmpty()||catId==null||modelId==null
                        ||groupId==null||spinner_item==null||date1.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),getString(R.string.fill_all_record),Toast.LENGTH_LONG).show();
                }
                else{
                sendMachine();
                }
            }
        });




        adapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, categoryListString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cat.setAdapter(adapter);
        spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       catId=categoryList.get(position).getCatId();
        getModels();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});

        modeladapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, modelListString);
        modeladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_model.setAdapter(modeladapter);
        spinner_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               modelId=modelList.get(position).getModelId();
                groupId=modelList.get(position).getGroupId();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.time_item, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTime.setAdapter(adapter1);
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                spinner_item = (String) parent.getItemAtPosition(position);
                if (spinner_item.equals((String)
                        parent.getItemAtPosition(1)))
                {spinner_item=90+"";
                    Toast.makeText(getActivity(),spinner_item,Toast.LENGTH_SHORT).show();
                }
                else if (spinner_item.equals((String)
                        parent.getItemAtPosition(2)))
                {spinner_item=60+"";
                    Toast.makeText(getActivity(),spinner_item,Toast.LENGTH_SHORT).show();
                }
                else if (spinner_item.equals((String)
                        parent.getItemAtPosition(3)))
                {spinner_item=30+"";
                    Toast.makeText(getActivity(),spinner_item,Toast.LENGTH_SHORT).show();
                }else if (spinner_item.equals((String)
                        parent.getItemAtPosition(4)))
                {spinner_item=7+"";
                    Toast.makeText(getActivity(),spinner_item,Toast.LENGTH_SHORT).show();
                }
                else if (spinner_item.equals((String)
                        parent.getItemAtPosition(0)))
                {spinner_item="";}


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentActivity= (FragmentActivity) activity;
    }
    public void sendMachine(){
progressBar3.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<ResponseBody> connection;
        GetData registerFunction = retrofit.create(GetData.class);

        connection = registerFunction.addUserMachine(userId,catId,modelId,groupId,state,workTime,spinner_item,date1.getText().toString());
        connection.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                String RegisterResult = null;
                try {
                    RegisterResult=response.body().string();
                    JSONObject jso = new JSONObject(RegisterResult);
                    String code = jso.getString("code");
                    if(code.equals("200")) {
                        Toast.makeText(fragmentActivity,getText(R.string.done),Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(fragmentActivity,getText(R.string.something_wrong),Toast.LENGTH_LONG).show();

                    }

                    } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    Toast.makeText(fragmentActivity, getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                }
                progressBar3.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressBar3.setVisibility(View.INVISIBLE);
                Toast.makeText(fragmentActivity, getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void getCategory(){
        progressBar3.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<categoryModelResult> connection;
        GetData registerFunction = retrofit.create(GetData.class);

        connection = registerFunction.category();
        connection.enqueue(new Callback<categoryModelResult>() {
            @Override
            public void onResponse(Call<categoryModelResult> call, Response<categoryModelResult> response) {

                try {


                    String RegisterResult = null;

                    categoryList = response.body().getCategory();
                    for (int i = 0; i < categoryList.size(); i++) {
                        categoryListString.add(categoryList.get(i).getCatName());
                    }
                    adapter.notifyDataSetChanged();
                    progressBar3.setVisibility(View.INVISIBLE);
                }
                catch (NullPointerException e){
                    Toast.makeText(fragmentActivity, getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    Toast.makeText(fragmentActivity, getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<categoryModelResult> call, Throwable t) {

                progressBar3.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });


    }
    public  void getModels(){


        progressBar3.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<modelResult> connection;
        GetData registerFunction = retrofit.create(GetData.class);

        connection = registerFunction.model(catId);
        connection.enqueue(new Callback<modelResult>() {
            @Override
            public void onResponse(Call<modelResult> call, Response<modelResult> response) {
             modelList.clear();
                modelListString.clear();
try {


    modelList = response.body().getModel();
    for (int i = 0; i < modelList.size(); i++) {
        modelListString.add(modelList.get(i).getModelName());
    }
    modeladapter.notifyDataSetChanged();

}catch (Exception ex){}

                progressBar3.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<modelResult> call, Throwable t) {

                progressBar3.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateLabel() {
        date1.setText(sdf.format(myCalendar.getTime()));

    }
   }
