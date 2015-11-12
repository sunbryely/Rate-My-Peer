package Service;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by zhuchen on 10/29/15.
 */
public class MemberServiceCenter {
    /**
     * get searched user's info
     * @param searched_id
     */

    public static RequestQueue requestQueue;
    private static MemberServiceCenter center;
    private static Context mCtx;
    public MemberServiceCenter(Context context) {
        mCtx = context;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

}
