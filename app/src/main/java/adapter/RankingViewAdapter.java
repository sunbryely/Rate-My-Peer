package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gevdev.stalky.R;
import com.gevdev.stalky.ViewPeopleActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bean.UserRanking;

/**
 * Created by kelsiedong on 12/2/15.
 */
public class RankingViewAdapter extends RecyclerView.Adapter<RankingViewAdapter.TextViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<UserRanking> items;

    public RankingViewAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
    }

    public void setItems(List<UserRanking> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextViewHolder(layoutInflater.inflate(R.layout.list_ranking_avg, parent, false));
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        UserRanking cur = items.get(position);
        String imageURL = "https://graph.facebook.com/" + cur.facebook_id + "/picture?type=large";
        Picasso.with(context).load(imageURL).into(holder.image);

        holder.name.setText(cur.username);
        holder.topNum.setText("Top " + (position + 1));

        holder.friendliness.setText(cur.rating_friendliness);
        holder.skills.setText(cur.rating_skill);
        holder.teamwork.setText(cur.rating_teamwork);
        holder.funfactor.setText(cur.rating_funfactor);
        holder.avg.setText(cur.rating_avg);

        holder.friendStar.setRating(Float.parseFloat(cur.rating_friendliness));
        holder.skillStar.setRating(Float.parseFloat(cur.rating_skill));
        holder.teamStar.setRating(Float.parseFloat(cur.rating_teamwork));
        holder.funStar.setRating(Float.parseFloat(cur.rating_funfactor));
        holder.avgStar.setRating(Float.parseFloat(cur.rating_avg));

        final String goTo = cur.facebook_id;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPeopleActivity.viewID = goTo;
                context.startActivity(new Intent(context, ViewPeopleActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class TextViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        TextView topNum;
        ImageView image;
        TextView name;

        TextView friendliness;
        TextView skills;
        TextView teamwork;
        TextView funfactor;
        TextView avg;

        RatingBar friendStar;
        RatingBar skillStar;
        RatingBar teamStar;
        RatingBar funStar;
        RatingBar avgStar;


        TextViewHolder(View view) {
            super(view);
            itemView = view;

            topNum = (TextView) view.findViewById(R.id.topNum);
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);

            friendliness = (TextView) view.findViewById(R.id.friendliness_score);
            skills = (TextView) view.findViewById(R.id.skills_score);
            teamwork = (TextView) view.findViewById(R.id.teamwork_score);
            funfactor = (TextView) view.findViewById(R.id.funfactor_score);
            avg = (TextView) view.findViewById(R.id.average_score);

            friendStar = (RatingBar) view.findViewById(R.id.friendliness_rating);
            skillStar = (RatingBar) view.findViewById(R.id.skills_rating);
            teamStar = (RatingBar) view.findViewById(R.id.teamwork_rating);
            funStar = (RatingBar) view.findViewById(R.id.funfactor_rating);
            avgStar = (RatingBar) view.findViewById(R.id.average_rating);


        }
    }


}
