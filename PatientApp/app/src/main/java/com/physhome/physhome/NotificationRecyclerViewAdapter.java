package com.physhome.physhome;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Meliksah on 7/14/2017.
 */

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.NotificationViewHolder> {
    private Context context;
    private List<NotificationModel> notificationModels;
    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView info;
        ImageButton summary;
        public NotificationViewHolder(View itemView) {
            super(itemView);
            info = (TextView) itemView.findViewById(R.id.notification_info_tv) ;
            summary = (ImageButton) itemView.findViewById(R.id.notification_infoButton);
        }
    }

    public NotificationRecyclerViewAdapter(Context context, List<NotificationModel> notificationModels) {
        this.context = context;
        this.notificationModels = notificationModels;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_item, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, final int position) {
        final NotificationModel model = notificationModels.get(position);
        // Bind the data efficiently with the holder.
        holder.info.setText(model.getDate()+"\n"+model.getInfo());
        holder.summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSummary(context,model.getSummary());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    private String countToTime(int count){
        int min, sec;
        min = count/60;
        sec = count%60;
        String str = String.format("%02d:%02d", min, sec);
        return str;
    }

    private void showSummary(Context context, final ExerciseSummary summary){
        final Dialog dialog = new Dialog(context,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.exercise_summary);
        dialog.setCancelable(true);
        dialog.setTitle(context.getString(R.string.exercise_summary));
        final TextView rating_tv = (TextView) dialog.findViewById(R.id.exercise_rating_tv);
        TextView time_tv        = (TextView) dialog.findViewById(R.id.exercise_passing_time_tv);
        TextView total_tv       = (TextView) dialog.findViewById(R.id.exercise_total_movement_tv);
        TextView correct_tv     = (TextView) dialog.findViewById(R.id.exercise_correct_pb_tv);
        TextView normal_tv      = (TextView) dialog.findViewById(R.id.exercise_normal_pb_tv);
        TextView fast_tv        = (TextView) dialog.findViewById(R.id.exercise_fast_pb_tv);
        TextView slow_tv        = (TextView) dialog.findViewById(R.id.exercise_slow_pb_tv);
        TextView wrong_tv       = (TextView) dialog.findViewById(R.id.exercise_wrong_pb_tv);
        TextView limb_error_tv  = (TextView) dialog.findViewById(R.id.exercise_limb_error_pb_tv);
        TextView chest_error_tv = (TextView) dialog.findViewById(R.id.exercise_chest_error_pb_tv);
        TextView both_error_tv  = (TextView) dialog.findViewById(R.id.exercise_limb_and_chest_error_pb_tv);
        ProgressBar correct_pb  = (ProgressBar) dialog.findViewById(R.id.exercise_correct_pb);
        ProgressBar normal_pb   = (ProgressBar) dialog.findViewById(R.id.exercise_normal_pb);
        ProgressBar fast_pb     = (ProgressBar) dialog.findViewById(R.id.exercise_fast_pb);
        ProgressBar slow_pb     = (ProgressBar) dialog.findViewById(R.id.exercise_slow_pb);
        ProgressBar wrong_pb    = (ProgressBar) dialog.findViewById(R.id.exercise_wrong_pb);
        ProgressBar limb_pb     = (ProgressBar) dialog.findViewById(R.id.exercise_limb_error_pb);
        ProgressBar chest_pb    = (ProgressBar) dialog.findViewById(R.id.exercise_chest_error_pb);
        ProgressBar both_pb     = (ProgressBar) dialog.findViewById(R.id.exercise_limb_and_chest_error_pb);
        RatingBar ratingBar     = (RatingBar) dialog.findViewById(R.id.exercise_ratingbar);
        Button ok_button        = (Button) dialog.findViewById(R.id.exercise_ok_button);
        final ImageView correct_detail_iv = (ImageView) dialog.findViewById(R.id.summary_correct_details_iv);
        final ImageView wrong_detail_iv = (ImageView) dialog.findViewById(R.id.summary_wrong_details_iv);
        final CardView correct_detail_cardView = (CardView) dialog.findViewById(R.id.summary_detailed_correct_cv);
        final CardView wrong_detail_cardView = (CardView) dialog.findViewById(R.id.summary_detailed_wrong_cv);
        Drawable correct_draw   = new ProgressDrawable(0xdd00ff00, 0x4400ff00);
        Drawable normal_draw    = new ProgressDrawable(0xdd00ffff, 0x4400ffff);
        Drawable fast_draw      = new ProgressDrawable(0xddffff00, 0x44ffff00);
        Drawable slow_draw      = new ProgressDrawable(0xddff00ff, 0x44ff00ff);
        Drawable wrong_draw     = new ProgressDrawable(0xddff0000, 0x44ff0000);
        Drawable limb_draw    = new ProgressDrawable(0xdd00ffff, 0x4400ffff);
        Drawable chest_draw      = new ProgressDrawable(0xddffff00, 0x44ffff00);
        Drawable both_draw      = new ProgressDrawable(0xddff00ff, 0x44ff00ff);
        final boolean[] correct_detail_state = {false};
        final boolean[] wrong_detail_state = {false};
        ratingBar.setRating((float) (1.0*summary.getDifficulty()/2));
        rating_tv.setText(String.valueOf(summary.getDifficulty()));
        ratingBar.setEnabled(false);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        int normal = summary.getNormal();
        int fast = summary.getFast();
        int slow = summary.getSlow();
        int total = summary.getTotal();
        int wrong = summary.getWrong();
        int limb = summary.getLimb();
        int chest = summary.getChest();
        int both = summary.getBoth();
        int exercise_time = summary.getExercise_time();
        double percentage_correct = (normal+fast+slow)*100.0/total;
        double percentage_normal  = normal*100.0/total;
        double percentage_fast    = fast*100.0/total;
        double percentage_slow    = slow*100.0/total;
        double percentage_wrong   = wrong*100.0/total;
        double percentage_limb    = limb*100.0/total;
        double percentage_chest   = chest*100.0/total;
        double percentage_both    = both*100.0/total;
        String correct_str        = "% "+String.format("%.1f",percentage_correct);
        String normal_str         = "% "+String.format("%.1f",percentage_normal);
        String fast_str           = "% "+String.format("%.1f",percentage_fast);
        String slow_str           = "% "+String.format("%.1f",percentage_slow);
        String wrong_str          = "% "+String.format("%.1f",percentage_wrong);
        String limb_str           = "% "+String.format("%.1f",percentage_limb);
        String chest_str          = "% "+String.format("%.1f",percentage_chest);
        String both_str           = "% "+String.format("%.1f",percentage_both);
        String time_str           = countToTime(exercise_time);
        time_tv.setText(time_str);
        total_tv.setText(String.valueOf(total));
        correct_tv.setText(correct_str);
        normal_tv.setText(normal_str);
        fast_tv.setText(fast_str);
        slow_tv.setText(slow_str);
        wrong_tv.setText(wrong_str);
        limb_error_tv.setText(limb_str);
        chest_error_tv.setText(chest_str);
        both_error_tv.setText(both_str);
        correct_pb.setProgressDrawable(correct_draw);
        normal_pb.setProgressDrawable(normal_draw);
        fast_pb.setProgressDrawable(fast_draw);
        slow_pb.setProgressDrawable(slow_draw);
        wrong_pb.setProgressDrawable(wrong_draw);
        limb_pb.setProgressDrawable(limb_draw);
        chest_pb.setProgressDrawable(chest_draw);
        both_pb.setProgressDrawable(both_draw);
        correct_pb.setProgress((int) percentage_correct);
        normal_pb.setProgress((int) percentage_normal);
        fast_pb.setProgress((int) percentage_fast);
        slow_pb.setProgress((int) percentage_slow);
        wrong_pb.setProgress((int) percentage_wrong);
        limb_pb.setProgress((int) percentage_limb);
        chest_pb.setProgress((int) percentage_chest);
        both_pb.setProgress((int) percentage_both);
        correct_detail_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correct_detail_state[0]){
                    correct_detail_iv.setImageResource(R.drawable.ic_action_down);
                    correct_detail_cardView.setVisibility(View.GONE);
                    correct_detail_state[0] = false;
                }else{
                    correct_detail_iv.setImageResource(R.drawable.ic_action_up);
                    correct_detail_cardView.setVisibility(View.VISIBLE);
                    correct_detail_state[0] = true;
                }
            }
        });
        wrong_detail_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wrong_detail_state[0]){
                    wrong_detail_iv.setImageResource(R.drawable.ic_action_down);
                    wrong_detail_cardView.setVisibility(View.GONE);
                    wrong_detail_state[0] = false;
                }else{
                    wrong_detail_iv.setImageResource(R.drawable.ic_action_up);
                    wrong_detail_cardView.setVisibility(View.VISIBLE);
                    wrong_detail_state[0] = true;
                }
            }
        });
        dialog.show();
    }

}
