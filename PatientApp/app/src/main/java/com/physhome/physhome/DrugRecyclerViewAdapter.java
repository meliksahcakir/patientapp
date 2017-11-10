package com.physhome.physhome;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Meliksah on 7/14/2017.
 */

public class DrugRecyclerViewAdapter extends RecyclerView.Adapter<DrugRecyclerViewAdapter.DrugViewHolder> {
    private Context context;
    private List<DrugModel> drugModels;

    public class DrugViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView info;
        public DrugViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.treatment_drug_title);
            info = (TextView) itemView.findViewById(R.id.treatment_drug_comment);
        }
    }

    public DrugRecyclerViewAdapter(Context context, List<DrugModel> drugModels) {
        this.context = context;
        this.drugModels = drugModels;
    }

    @Override
    public DrugViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.treatment_drug_item, parent, false);
        return new DrugViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DrugViewHolder holder, final int position) {
        DrugModel model = drugModels.get(position);
        holder.title.setText(model.getName());
        holder.info.setText(model.getComment()+"\n"+context.getString(R.string.amount)+" "+model.getHow_many());
    }

    @Override
    public int getItemCount() {
        return drugModels.size();
    }
}
