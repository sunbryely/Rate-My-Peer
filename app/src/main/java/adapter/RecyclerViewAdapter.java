package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gevdev.stalky.R;
import com.gevdev.stalky.ViewPeopleActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Service.MemberServiceCenter;
import bean.Comment;

/**
 * Created by kelsiedong on 11/29/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TextViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<Comment> items;
//    TextViewHolder globalHolder;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.items = new ArrayList<>();
    }

    public void setItems(List<Comment> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextViewHolder(layoutInflater.inflate(R.layout.list_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        Comment cur = items.get(position);
        String imageURL = "https://graph.facebook.com/" + cur.user_id_from + "/picture?type=large";
        Picasso.with(context).load(imageURL).into(holder.image);

        //TODO get the name of user
        String nameURL = "https://graph.facebook.com/" + cur.user_id_from;
//        globalHolder = holder;
//
//        JsonObjectRequest jsonRequest = new JsonObjectRequest
//                (Request.Method.GET, nameURL, null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//                        Log.e("nameURL", "jsonObject = " + jsonObject.toString());
//                        try {
//                            globalHolder.name.setText(jsonObject.getString("name"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        volleyError.printStackTrace();
//                    }
//
//                });
//
//        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
//                50000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        MemberServiceCenter.requestQueue.add(jsonRequest);


        holder.updateAt.setText(cur.updated_at);
        holder.comment.setText(cur.comment);

//        final int curPosition = position;
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ViewPeopleActivity.class);
//                intent.putExtra("item", items.get(curPosition));
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class TextViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView updateAt;
        TextView comment;
        View itemView;

        TextViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            updateAt = (TextView) view.findViewById(R.id.updateAt);
            comment = (TextView) view.findViewById(R.id.comment);
            itemView = view;

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
//                }
//            });
        }
    }


}
