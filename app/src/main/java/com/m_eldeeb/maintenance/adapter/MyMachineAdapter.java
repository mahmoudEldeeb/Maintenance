package com.m_eldeeb.maintenance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.interfaces.Communication;
import com.m_eldeeb.maintenance.models.MachineModel;

import java.util.List;

/**
 * Created by melde on 7/23/2017.
 */

public class MyMachineAdapter extends RecyclerView.Adapter<MyMachineAdapter.ViewHolder> {
    private Context context;
    List<MachineModel>machinelList;



    public MyMachineAdapter(Context c, List<MachineModel>list) {

        this.context = c;
        machinelList=list;
    }

    @Override
    public MyMachineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_of_machine, parent, false);
        MyMachineAdapter.ViewHolder holder = new MyMachineAdapter.ViewHolder(row);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyMachineAdapter.ViewHolder holder, int position) {
        holder.catName.setText(machinelList.get(position).getCatName());
        holder.modelName.setText(machinelList.get(position).getModelName());
        holder.hour.setText(machinelList.get(position).getWorkTime());
    }

    @Override
    public int getItemCount() {
        return machinelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextView catName;
         TextView modelName;

        TextView hour;
         Communication con;

        public ViewHolder(View itemView) {
            super(itemView);
            //check = (CheckBox) itemView.findViewById(R.id.check);
            catName = (TextView) itemView.findViewById(R.id.catName);
            modelName = (TextView) itemView.findViewById(R.id.modelName);
            hour = (TextView) itemView.findViewById(R.id.hour);
            con = (Communication) context;
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

              MachineModel model = machinelList.get(adapterPosition);
             con.onclick(model);
        }
    }
}
