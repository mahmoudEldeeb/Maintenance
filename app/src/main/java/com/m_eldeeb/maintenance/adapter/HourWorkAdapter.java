package com.m_eldeeb.maintenance.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.activity.ChangeParts;
import com.m_eldeeb.maintenance.interfaces.Communication;
import com.m_eldeeb.maintenance.interfaces.GetData;
import com.m_eldeeb.maintenance.models.MachineModel;
import com.m_eldeeb.maintenance.models.PartModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by melde on 7/23/2017.
 */

public class HourWorkAdapter  extends RecyclerView.Adapter<HourWorkAdapter.ViewHolder> {
    private Context context;
    List<MachineModel> machinelList;
    Communication con;


    public HourWorkAdapter(Context c, List<MachineModel> list) {

        this.context = c;
        machinelList = list;
    }

    @Override
    public HourWorkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_of_add_hour
                , parent, false);
        HourWorkAdapter.ViewHolder holder = new HourWorkAdapter.ViewHolder(row);
        return holder;

    }

    @Override
    public void onBindViewHolder(HourWorkAdapter.ViewHolder holder, int position) {

        holder.catName.setText(machinelList.get(position).getCatName());
        holder.modelName.setText(machinelList.get(position).getModelName());
        }

    @Override
    public int getItemCount() {
        return machinelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView catName;
        TextView modelName;

        EditText workHour;
        Button addButton;

        public ViewHolder(View itemView) {
            super(itemView);
            catName = (TextView) itemView.findViewById(R.id.catName);
            modelName = (TextView) itemView.findViewById(R.id.modelName);
            workHour = (EditText) itemView.findViewById(R.id.workHour2);
            addButton = (Button) itemView.findViewById(R.id.addButton2);
            addButton.setOnClickListener(this);
con= (Communication) context;

        }

        @Override
        public void onClick(View v) {
            final int adapterPosition = getAdapterPosition();
            String e=workHour.getText().toString();
            if(e.isEmpty()){ Toast.makeText(context, context.getText(R.string.enterHourWork), Toast.LENGTH_SHORT).show();
            }
            else{
            updateHourWork(machinelList.get(adapterPosition).getMacineRowId(),e,adapterPosition);
            }

        }
        public void updateHourWork(String row, String hour, final int p){
            String userId = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .getString("id", "-1");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.BAS_URL))
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
                            machinelList.remove(p);
if(machinelList.size()==0){
    SharedPreferences.Editor editor = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit();

    editor.putBoolean("hour", false);
editor.commit();
    con.onclick(new MachineModel());
    Intent intent=new Intent(context, ChangeParts.class);
context.startActivity(intent);}
                            notifyDataSetChanged();
                            workHour.setText("");
                            Toast.makeText(context, context.getText(R.string.done), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, context.getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                        }



                    } catch (IOException e) {
                       Toast.makeText(context, context.getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, context.getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();


                }
            });

        }
    }
}