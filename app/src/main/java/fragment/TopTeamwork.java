package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.gevdev.stalky.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import Service.MemberServiceCenter;
import adapter.RankingViewAdapter;
import bean.UserRanking;

/**
 * Created by kelsiedong on 12/2/15.
 */
public class TopTeamwork extends Fragment{

    private List<UserRanking> userRankingList = new ArrayList<>();
    RankingViewAdapter rankingViewAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.rankings_avg, container, false);
        RecyclerView rankingView = (RecyclerView) view.findViewById(R.id.rankingView);
        rankingView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        rankingViewAdapter = new RankingViewAdapter(this.getActivity(), 3);
        rankingView.setAdapter(rankingViewAdapter);

        getData();

        return view;
    }

    private void getData() {
        String URL = String.format("http://54.149.222.140/top/%s", "teamwork");
        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.e("Top", "jsonObject = " + jsonArray.toString());
                        try {
                            userRankingList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                UserRanking cur = new UserRanking();
                                cur.facebook_id = jsonArray.getJSONObject(i).getString("facebook_id");
                                cur.username = jsonArray.getJSONObject(i).getString("name");
                                cur.rating_friendliness = String.format("%.1f", jsonArray.getJSONObject(i).getDouble("friendliness"));
                                cur.rating_skill = String.format("%.1f", jsonArray.getJSONObject(i).getDouble("skill"));
                                cur.rating_teamwork = String.format("%.1f", jsonArray.getJSONObject(i).getDouble("teamwork"));
                                cur.rating_funfactor = String.format("%.1f", jsonArray.getJSONObject(i).getDouble("funfactor"));
                                cur.rating_avg = String.format("%.1f", jsonArray.getJSONObject(i).getDouble("avg"));
                                userRankingList.add(cur);
                            }
                            rankingViewAdapter.setItems(userRankingList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }

                });

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MemberServiceCenter.requestQueue.add(jsonRequest);

    }
}
