package com.physhome.physhome;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Meliksah on 8/19/2017.
 */

public class FAQRecyclerViewAdapter extends RecyclerView.Adapter<FAQRecyclerViewAdapter.FAQViewHolder>{
    private Context context;
    private ArrayList<FAQModel> faqModels;

    public class FAQViewHolder extends RecyclerView.ViewHolder{
        TextView question_tv;
        TextView answer_tv;
        public FAQViewHolder(View itemView) {
            super(itemView);
            question_tv = (TextView) itemView.findViewById(R.id.faq_question_tv);
            answer_tv = (TextView) itemView.findViewById(R.id.faq_answer_tv);
        }
    }

    public FAQRecyclerViewAdapter(Context context, ArrayList<FAQModel> faqModels) {
        this.context = context;
        this.faqModels = faqModels;
    }

    @Override
    public FAQViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faq_item, parent, false);
        return new FAQViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(FAQViewHolder holder, final int position) {
        FAQModel model = faqModels.get(position);
        holder.question_tv.setText(model.getQuestion());
        holder.answer_tv.setText(model.getAnswer());
    }
    @Override
    public int getItemCount() {
        return faqModels.size();
    }
}

