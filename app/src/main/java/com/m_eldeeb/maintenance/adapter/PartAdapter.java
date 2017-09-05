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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.m_eldeeb.maintenance.MainActivity;
import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.activity.Login;
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

/**
 * Created by melde on 7/23/2017.
 */

public class PartAdapter extends RecyclerView.Adapter<PartAdapter.ViewHolder> {
private Context context;
        List<PartModel>partlList;
        Communication con;


    public PartAdapter(Context c, List<PartModel> list) {

        this.context = c;
        partlList=list;
    }

    @Override
    public PartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part, parent, false);
        PartAdapter.ViewHolder holder = new PartAdapter.ViewHolder(row);
        return holder;

    }

    @Override
    public void onBindViewHolder(PartAdapter.ViewHolder holder, int position) {

    holder.partName.setText(partlList.get(position).getPartName());
    holder.lifeSpan.setText(partlList.get(position).getMainLivetime());
    holder.workHour.setText(partlList.get(position).getCrruintLivetime());
   }

    @Override
    public int getItemCount() {
        return partlList.size();
    }

class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView lifeSpan;
    TextView partName;

    TextView workHour;
    Communication con;

     Dialog dialog = new Dialog(context);

     Dialog dialog2 = new Dialog(context);
    ImageButton hourButton,spanButton;
    public ViewHolder(View itemView) {
        super(itemView);
        partName = (TextView) itemView.findViewById(R.id.partName);
        lifeSpan = (TextView) itemView.findViewById(R.id.lifeSpan);
        workHour = (TextView) itemView.findViewById(R.id.workHour);
        hourButton = (ImageButton) itemView.findViewById(R.id.hourButton);

        spanButton = (ImageButton) itemView.findViewById(R.id.spanButton);
//        con = (Communication) context;
        hourButton.setOnClickListener(this);
        spanButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        final int adapterPosition = getAdapterPosition();


        if(v.getId()==R.id.hourButton) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.row_spinner);
            dialog.setCancelable(true);
            TextView title= (TextView) dialog.findViewById(R.id.title);
            title.setText(R.string.change_number_of_hour_work);
            final EditText edittext = (EditText) dialog.findViewById(R.id.editText1);
            Button ok = (Button) dialog.findViewById(R.id.button1);
            Button cancel = (Button) dialog.findViewById(R.id.button2);

    ok.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            updateworkHour(partlList.get(adapterPosition).getUserMacineId()
                    ,edittext.getText().toString(),adapterPosition);

             }
    });
    cancel.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
dialog.dismiss();
             }
    });
    dialog.show();

}
else if(v.getId()==R.id.spanButton){
            dialog2 = new Dialog(context);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setContentView(R.layout.row_spinner);
            dialog2.setCancelable(true);
            TextView title= (TextView) dialog2.findViewById(R.id.title);
            title.setText(R.string.change_span);
            final EditText edittext = (EditText) dialog2.findViewById(R.id.editText1);
            Button ok = (Button) dialog2.findViewById(R.id.button1);
            Button cancel = (Button) dialog2.findViewById(R.id.button2);
            ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    updatespan(partlList.get(adapterPosition).getUserMacineId()
                            ,edittext.getText().toString(),adapterPosition);

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                }
            });
            dialog2.show();


        }

    }
    public void updateworkHour(String mId, final String w, final int p){

        String userId = context.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getString("id", "-1");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<ResponseBody> connection;
        GetData loginObgect = retrofit.create(GetData.class);
        connection = loginObgect.changeSpearpart(userId,mId,w);
        connection.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                String RegisterResult = null;

                try {
                    RegisterResult = response.body().string();
                    JSONObject jso = new JSONObject(RegisterResult);
                    String code = jso.getString("code");

                    if(code.equals("200")){
                        partlList.get(p).setCrruintLivetime(w);
                        notifyDataSetChanged();
                        Toast.makeText(context, context.getText(R.string.done), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    } else {
                        dialog.dismiss();
                    }


                } catch (IOException e) {
                    Toast.makeText(context, context.getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                } catch (JSONException e) {
                    Toast.makeText(context, context.getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, context.getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });



    }
    public void updatespan(String mId, final String w, final int p){

       String userId = context.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getString("id", "-1");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.BAS_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<ResponseBody> connection;
        GetData loginObgect = retrofit.create(GetData.class);
        connection = loginObgect.updateSpearpartLivetime(userId,mId,w);
        connection.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                String RegisterResult = null;

                try {
                    RegisterResult = response.body().string();
                    JSONObject jso = new JSONObject(RegisterResult);
                    String code = jso.getString("code");

                    if(code.equals("200")){
                        partlList.get(p).setMainLivetime(w);
                        notifyDataSetChanged();
                        Toast.makeText(context, context.getText(R.string.done), Toast.LENGTH_SHORT).show();
                        dialog2.dismiss();

                    } else {
                        dialog2.dismiss();
                       }


                } catch (IOException e) {
                    Toast.makeText(context, context.getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                    dialog2.dismiss();
                    e.printStackTrace();
                } catch (JSONException e) {
                    Toast.makeText(context, context.getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();

                    dialog2.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, context.getText(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                dialog2.dismiss();

            }
        });



            }
}
}