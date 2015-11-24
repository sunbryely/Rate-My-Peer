package bean;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;


/**
 * Created by zhuchen on 10/29/15.
 */
public class User implements Serializable{
    private static final long serialVersionUID = -5286096859502269561L;

    private String userId;
    private String friendiScore;
    private String skillsScore;
    private String teamworkScore;
    private String funScore;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendiScore() {
        return friendiScore;
    }

    public void setFriendiScore(String friendlinessScore) {
        this.friendiScore = friendlinessScore;
    }

    public String getSkillsScore() {
        return skillsScore;
    }

    public void setSkillsScore(String skillsScore) {
        this.skillsScore = skillsScore;
    }

    public String getTeamworkScore() {
        return teamworkScore;
    }

    public void setTeamworkScore(String teamworkScore) {
        this.teamworkScore = teamworkScore;
    }

    public String getFunScore() {
        return funScore;
    }

    public void setFunScore(String funScore) {
        this.funScore = funScore;
    }

    public static User fromJson(JSONObject obj){
        User result = null;
        try{
            result = new User();
            result.userId = obj.optString("user_id");
            result.friendiScore = obj.optString("friendiScore");
            result.skillsScore = obj.optString("skillsScore");
            result.teamworkScore = obj.optString("teamworkScore");
            result.funScore = obj.optString("funScore");

        }catch(Exception e){
            Log.e("JsonErr",""+e);
        }
        return result;
    }




}
