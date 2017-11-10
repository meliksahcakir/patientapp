package com.physhome.physhome;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Meliksah on 7/14/2017.
 */

public class SelectExerciseRecyclerViewAdapter  extends RecyclerView.Adapter<SelectExerciseRecyclerViewAdapter.SelectExerciseViewHolder> {
    private Context context;
    private ArrayList<ExerciseModel> exerciseList;
    private ArrayList<ExerciseInfoModel> exerciseInfoList;
    private OnRecyclerViewItemClickListener listener;
    int row_index = -1;
    public SelectExerciseRecyclerViewAdapter(Context context, ArrayList<ExerciseModel> exerciseList, ArrayList<ExerciseInfoModel> exerciseInfoList, OnRecyclerViewItemClickListener listener) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.exerciseInfoList = exerciseInfoList;
        this.listener = listener;
    }

    @Override
    public SelectExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_list_item, parent, false);
        return new SelectExerciseViewHolder(itemView);
    }

    public class SelectExerciseViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView info;
        LinearLayout linearLayout;

        public SelectExerciseViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.exercise_imageview);
            info = (TextView) itemView.findViewById(R.id.exercise_info_tv);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.exercise_layout);
        }
    }

    @Override
    public void onBindViewHolder(SelectExerciseViewHolder holder,final int position) {
        final ExerciseModel model = exerciseList.get(position);
        final ExerciseInfoModel infoModel = exerciseInfoList.get(position);
        /*TODO change resource id */
        int resourceId =  context.getResources().getIdentifier("bacakegzersizi", "drawable", context.getPackageName());
        if(!infoModel.getImage_id().equals("000000000000000000000000")){
            String path = context.getFilesDir()+"/"+infoModel.getImage_id()+".png";
            Bitmap bmImg = BitmapFactory.decodeFile(path);
            holder.imageView.setImageBitmap(bmImg);
        }else{
            holder.imageView.setImageResource(resourceId);
        }
        holder.info.setText(infoModel.getName()+"\n"+model.getRepeat()+" "+context.getString(R.string.repeat)+"\n"+model.getTotal_day()+" "+context.getString(R.string.days));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerViewItemClicked(position);
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.linearLayout.setBackgroundResource(R.drawable.selection_bg);
        } else {
            holder.linearLayout.setBackgroundResource(0);
        }
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}

