package com.m_eldeeb.maintenance.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.adapter.MyMachineAdapter;
import com.m_eldeeb.maintenance.helper;
import com.m_eldeeb.maintenance.interfaces.GetData;
import com.m_eldeeb.maintenance.models.MachineModel;
import com.m_eldeeb.maintenance.models.MachineModelResult;
import com.m_eldeeb.maintenance.models.categoryModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.R.id.progressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateWorkedHoure extends Fragment {


    public UpdateWorkedHoure() {
        // Required empty public constructor
    }

Spinner machine;
Button add;
    EditText hour;
    ProgressBar progressBar;
    ArrayAdapter<String> adapter,modeladapter;
    String[] title;
    String spinner_item;
   String machineId="";
    String userId;
    List<MachineModel> machineModelList=new ArrayList<>();
    List<String>categoryListString=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_update_worked_houre, container, false);
        machine = (Spinner) view.findViewById(R.id.machine);
        hour= (EditText) view.findViewById(R.id.hour);
        progressBar= (ProgressBar) view.findViewById(R.id.progressBar3);

        add= (Button) view.findViewById(R.id.add);
        categoryListString.add(getString(R.string.choose));
         userId = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("id", "-1");
        adapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, categoryListString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        machine.setAdapter(adapter);
        machine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             //   catId=categoryList.get(position).getCatId();
               // getModels();

                if(machineModelList.size()!=0&&position!=0) {

                    machineId=machineModelList.get(position-1).getMacineRowId();
                }

                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(new helper(getActivity()).isOnline()) {
            getUserMachine();
        }else{
            Toast.makeText(getActivity(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

        }
        add.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                     String h=  hour.getText().toString();
                                       if(machineId==""|| h.isEmpty()){
                                           Toast.makeText(getActivity(),getText(R.string.fill),Toast.LENGTH_SHORT).show();

                                       }

                                     else  {updateHourWork(machineId,h);}

                                   }
                               }
        );
        return view;    }


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

                    for (int i=0;i<machineModelList.size();i++){
                        categoryListString.add(getString(R.string.type)+" : "+machineModelList.get(i).getCatName()+"   "+getString(R.string.model1)+" : "+machineModelList.get(i).getModelName());
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);

                }
                else
                    Toast.makeText(getActivity(), getText(R.string.no_machine), Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                Toast.makeText(getActivity(), getText(R.string.no_machine), Toast.LENGTH_SHORT).show();

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
    public void updateHourWork(String row, String hour){
        progressBar.setVisibility(View.VISIBLE);

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
                progressBar.setVisibility(View.INVISIBLE);

                try {
                    RegisterResult = response.body().string();

                    JSONObject jso = new JSONObject(RegisterResult);
                    String code= jso.getString("code");

                    if(code.equals("200")){
                        Toast.makeText(getActivity(), getText(R.string.done), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                    }



                } catch (IOException e) {
                    Toast.makeText(getActivity(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);


            }
        });

    }
}
