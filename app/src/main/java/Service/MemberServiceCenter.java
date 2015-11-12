package Service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.logging.Handler;
import java.util.logging.Logger;

import bean.User;

/**
 * Created by zhuchen on 10/29/15.
 */
public class MemberServiceCenter {
    /**
     * get searched user's info
     * @param searched_id
     */
    private static final String TAG = "ViewPeopleFragment";
    private static RequestQueue requestQueue;
    private static MemberServiceCenter center;
    private static Context mCtx;
    //public static boolean isFinished = false;
    private MemberServiceCenter(Context context) {
        mCtx = context;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }
    public static MemberServiceCenter getInstance(Context context) {
        if (center == null) center = new MemberServiceCenter(context);
        return center;
    }
    public void getSearchedUserInfo(String searched_id, Handler handler) {

        String URL= String.format("/users/%s", searched_id);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e(TAG, "jsonObject = " + jsonObject.toString());


                        try {
                            //User user;
                            JSONArray jArray = jsonObject.getJSONArray("searched_id");
                            Log.e(TAG, "contacts = " + jArray.toString());
                            JSONObject obj = jsonObject.getJSONObject("");
                            //String id = dfkls
                            //user.setUserId(id);


                        } catch (JSONException e) {
                            Log.e(TAG, "error");
                            e.printStackTrace();

                        }
                        //isFinished = true;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }

                });

            requestQueue.add(jsonRequest);
            //isFinished = false;
        //while(!isFinished);


    }
}
