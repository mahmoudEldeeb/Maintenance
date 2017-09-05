package com.m_eldeeb.maintenance.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.activity.ChangeParts;
import com.m_eldeeb.maintenance.interfaces.Communication;
import com.m_eldeeb.maintenance.interfaces.GetData;
import com.m_eldeeb.maintenance.models.ChangeModel;
import com.m_eldeeb.maintenance.models.MachineModel;

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
 * Created by melde on 7/24/2017.
 */

public class ChangePartAdapter extends RecyclerView.Adapter<ChangePartAdapter.ViewHolder> {
    private Context context;
    List<ChangeModel> machinelList;



    public ChangePartAdapter(Context c, List<ChangeModel>list) {

        this.context = c;
        machinelList=list;
    }

    @Override
    public ChangePartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_change_part, parent, false);
        ChangePartAdapter.ViewHolder holder = new ChangePartAdapter.ViewHolder(row);
        return holder;

    }

    @Override
    public void onBindViewHolder(ChangePartAdapter.ViewHolder holder, int position) {
        holder.catName.setText(machinelList.get(position).getModelName());
        holder.modelName.setText(machinelList.get(position).getModelName());
        holder.hour.setText(machinelList.get(position).getCrruintLivetime());
        holder.partName.setText(machinelList.get(position).getPartName());
    }

    @Override
    public int getItemCount() {
        return machinelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView catName;
        TextView modelName;

        TextView hour;
        TextView partName;
        Communication con;
Button button;
        public ViewHolder(View itemView) {
            super(itemView);
            catName = (TextView) itemView.findViewById(R.id.catName);
            modelName = (TextView) itemView.findViewById(R.id.modelName);
            hour = (TextView) itemView.findViewById(R.id.hour);
            partName=(TextView) itemView.findViewById(R.id.partName);

            button= (Button) itemView.findViewById(R.id.button);
//            con = (Communication) context;
            button.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            updateHourWork(machinelList.get(adapterPosition).getUserMacineId(),adapterPosition);
        }

        public void updateHourWork(String row, final int p){
            String userId = context.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                    .getString("id", "-1");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.BAS_URL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final Call<ResponseBody> connection;
            GetData loginObgect = retrofit.create(GetData.class);
            connection = loginObgect.changeSpearpart(userId,row,"0");
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

                            notifyDataSetChanged();
                           // workHour.setText("");
                            Toast.makeText(context, context.getText(R.string.done), Toast.LENGTH_SHORT).show();


                            if(machinelList.size()==0){

                                con.onclick(new MachineModel());
                               }




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
