package com.physhome.physhome;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Meliksah on 7/14/2017.
 */

public class ExerciseRecyclerViewAdapter extends RecyclerView.Adapter<ExerciseRecyclerViewAdapter.ExerciseViewHolder> {
    private Context context;
    private List<ExerciseModel> exerciseModels;
    private List<ExerciseInfoModel> exerciseInfoModels;
    private OnRecyclerViewItemClickListener listener;

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;
        TextView info;
        CardView cardView;
        public ExerciseViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.treatment_exercise_cardview);
            title = (TextView) itemView.findViewById(R.id.treatment_exercise_title_tv) ;
            info = (TextView) itemView.findViewById(R.id.treatment_exercise_info_tv) ;
            imageView = (ImageView) itemView.findViewById(R.id.treatment_exercise_image);
        }
    }

    public ExerciseRecyclerViewAdapter(Context context, List<ExerciseModel> exerciseModels, List<ExerciseInfoModel> exerciseInfoModels,OnRecyclerViewItemClickListener listener) {
        this.context = context;
        this.exerciseModels = exerciseModels;
        this.exerciseInfoModels = exerciseInfoModels;
        this.listener = listener;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.treatment_exercise_item, parent, false);
        return new ExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, final int position) {
        final ExerciseModel model = exerciseModels.get(position);
        final ExerciseInfoModel infoModel = exerciseInfoModels.get(position);
        holder.title.setText(infoModel.getName());
        final String info = model.getRepeat()+" "+context.getString(R.string.repeat)+"\n"+model.getTotal_day()+" "+context.getString(R.string.days);
        holder.info.setText(info);
        /*TODO change resource id */
        int resourceId =  context.getResources().getIdentifier("bacakegzersizi", "drawable", context.getPackageName());
        if(!infoModel.getImage_id().equals("000000000000000000000000")){
            String path = context.getFilesDir()+"/"+infoModel.getImage_id()+".png";
            Bitmap bmImg = BitmapFactory.decodeFile(path);
            holder.imageView.setImageBitmap(bmImg);
        }else{
            holder.imageView.setImageResource(resourceId);
        }
        holder.imageView.setImageResource(resourceId);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerViewItemClicked(position);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerViewItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseModels.size();
    }
}
