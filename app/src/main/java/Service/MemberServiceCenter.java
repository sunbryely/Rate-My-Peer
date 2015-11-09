package Service;

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
    public static void getSearchedUserInfo(String searched_id) {


       /* User result = null;
        String uri = String.format("/users/%s", searched_id);
        HttpResponse response = ServiceCenter.get(uri);
        if(response != null && response.getStatusLine().getStatusCode() == 200) {
            String resp = ResponseHelper.parseString(response);
            try {
                JSONObject obj = new JSONObject(resp);
                result = User.fromJson(obj);
            }catch(Exception e){
                Log.e("GetInfoErr",""+ e);
            }
        }
        return result;
    }*/
        String URL = String.format("/users/%s", searched_id);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e(TAG, "jsonObject = " + jsonObject.toString());


                        try {
                            JSONArray jArray = jsonObject.getJSONArray("searched_id");
                            Log.e(TAG, "contacts = " + jArray.toString());
                            JSONObject obj = jsonObject.getJSONObject("");

                        } catch (JSONException e) {
                            Log.e(TAG, "error");
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }

                });
        requestQueue = Volley.newRequestQueue(this); // Parameter for
        requestQueue.add(jsonRequest);               // newRequestQueue is a Context
    }
}
