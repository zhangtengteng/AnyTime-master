package com.luna.anytime;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

/**
 * Created by lzw on 14-9-11.
 */
public class AVService
{
	public static String ptmId;
    public static void countDoing(String doingObjectId,
            CountCallback countCallback)
    {
        AVQuery<AVObject> query = new AVQuery<AVObject>("DoingList");
        query.whereEqualTo("doingListChildObjectId", doingObjectId);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -10);
        // query.whereNotEqualTo("userObjectId", userId);
        query.whereGreaterThan("createdAt", c.getTime());
        query.countInBackground(countCallback);
    }
    
    //Use callFunctionMethod
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void getAchievement(String userObjectId)
    {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("userObjectId", userObjectId);
        AVCloud.callFunctionInBackground("hello",
                parameters,
                new FunctionCallback()
                {
                    @Override
                    public void done(Object object, AVException e)
                    {
                        if (e == null)
                        {
                            Log.e("at", object.toString());// processResponse(object);
                        }
                        else
                        {
                            // handleError();
                        }
                    }
                });
    }
    
    public static void createDoing(String userId, String doingObjectId)
    {
        AVObject doing = new AVObject("DoingList");
        doing.put("userObjectId", userId);
        doing.put("doingListChildObjectId", doingObjectId);
        doing.saveInBackground();
    }
    
    public static void requestPasswordReset(String email,
            RequestPasswordResetCallback callback)
    {
        AVUser.requestPasswordResetInBackground(email, callback);
        
    }
    
    public static void requestPSentCond(String phone,
            RequestMobileCodeCallback callback)
    {
        AVUser.requestMobilePhoneVerifyInBackgroud(phone, callback);
        
    }
    
    public static void findDoingListGroup(FindCallback<AVObject> findCallback)
    {
        AVQuery<AVObject> query = new AVQuery<AVObject>("DoingListGroup");
        query.orderByAscending("Index");
        query.findInBackground(findCallback);
    }
    
    public static void findChildrenList(String groupObjectId,
            FindCallback<AVObject> findCallback)
    {
        AVQuery<AVObject> query = new AVQuery<AVObject>("DoingListChild");
        query.orderByAscending("Index");
        query.whereEqualTo("parentObjectId", groupObjectId);
        query.findInBackground(findCallback);
    }
    
    public static void initPushService(Context ctx)
    {
        PushService.setDefaultPushCallback(ctx, LoginActivity.class);
        PushService.subscribe(ctx, "public", LoginActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground();
    }
    
    //username, userphoto, userSex, userbrithday,address   String username, String password, String email
    public static void signUp(String password, String phone, String username,
            String userSex, String userbrithday,String str,byte[] byteArrays,boolean mpv,SignUpCallback signUpCallback)
    {
        AVUser user = new AVUser();
        //user.setUsername(username);
        user.setPassword(password);
        user.put("mobilePhoneNumber", phone);
        // user.put("headImgUrl", userphoto);
        // user.put("nickName", username);
        user.setUsername(username);
        user.put("gender", userSex);
        user.put("birth", userbrithday);
        user.put("str", str);
        user.put("mpv", mpv);
        
        
        AVFile avFile = new AVFile("walking in Dubai", byteArrays);
        try {
			avFile.save();
		} catch (AVException e) {
			e.printStackTrace();
		}
        user.put("file", avFile);
        // user.put("province", address);
        // user.put("city", address);
        //user.put("district", address);
        user.signUpInBackground(signUpCallback);
    }
    
    /***
     * 私⼈人教练真实信息(PTRealInfo)
     * @param realName
     * @param identityNumber
     * @param verifiedClass
     * @param verifiedNumber
     * @param uid
     */
    public static void pTRealInfo(String realName, String identityNumber,
            String verifiedClass, String verifiedNumber, String uid,SaveCallback SaveCallback)
    {
        AVObject gameScore = new AVObject("PTRealInfo");
        gameScore.put("realName", realName);
        gameScore.put("identityNumber", "identityNumber");
        gameScore.put("verifiedClass", verifiedClass);
        gameScore.put("verifiedNumber", verifiedNumber);
        gameScore.put("uid", uid);
        gameScore.saveInBackground(SaveCallback);
      
    }
    
    /***
     * 3.私教会员(PTMember)
                            这是由私教创建的会员信息
     * @param String ptHeadImgUrl,
     * @param  String ptmName,
     * @param 
     * @param 
     * @param 
     */
    public static void pTMemberinfo( String ptmNickName,
            String ptmGender, String ptmBirth,String ptmPhone,String ptmEmail,String uid,byte[] icon,SaveCallback SaveCallback)
    {
        AVObject member = new AVObject("PTMember");
        member.put("ptmNickName",ptmNickName);
        member.put("ptmGender",ptmGender);
        member.put("ptmBirth", ptmBirth);
        member.put("ptmPhone", ptmPhone);
        member.put("ptmEmail", ptmEmail);
        member.put("puid", uid);
        AVFile avFile = new AVFile("ptmIcon", icon);
        try {
			avFile.save();
		} catch (AVException e) {
			e.printStackTrace();
		}
        member.put("ptmIcon", avFile);
        member.saveInBackground(SaveCallback);

    }
    /***
     * 添加 reshen ExerciseName记录
     * @param 
     */
    public static void addExerciseName( String ptmNickName,String uid,String ptmId,
    		SaveCallback SaveCallback)
    {
    	AVObject member = new AVObject("reshenRecord");
    	member.put("exerciseName",ptmNickName);
    	member.put("uid",uid);
    	member.put("ptmId",ptmId);
    	member.saveInBackground(SaveCallback);
    	
    }
    /***
     * 添加  抗阻力 ExerciseName记录
     * @param 
     */
    public static void addKangExerciseName( String ptmNickName,String uid,String ptmId,
    		SaveCallback SaveCallback)
    {
    	AVObject member = new AVObject("kangRecord");
    	member.put("exerciseName",ptmNickName);
    	member.put("uid",uid);
    	member.put("ptmId",ptmId);
    	member.saveInBackground(SaveCallback);
    	
    }
    
    /***
     * 添加  zhuti 记录
     * @param 
     */
    public static void addZhuTiName( String ptmNickName1, String ptmNickName2, String ptmNickName3,String uid,String ptmId,
    		SaveCallback SaveCallback)
    {
    	AVObject member = new AVObject("zhutiRecord");
    	member.put("themeTitle1",ptmNickName1);
    	member.put("themeTitle2",ptmNickName2);
    	member.put("themeTitle3",ptmNickName3);
    	member.put("uid",uid);
    	member.put("ptmId",ptmId);
    	member.saveInBackground(SaveCallback);
    	
    }
    

    /***
     * 课时阶段(CourseStage)
     * @param classTotal
     * @param classTimes
     * @param classLeft
     * @param ptMemberId
     * @param SaveCallback
     */
//    public static void courseStage( String classTotal,
//    		Array classTimes,timestamp firstClassTime,int classLeftString,String ptMemberId,SaveCallback SaveCallback)
//    {
//    	classTimes.
//        AVObject member = new AVObject("CourseStage");
//        member.put("classTotal",classTotal);
//        member.put("classTimes",classTimes);
//        member.put("firstClassTime",firstClassTime);
//        member.put("classLeft", classLeft);
//        member.put("ptMemberId", ptMemberId);
//        member.saveInBackground(SaveCallback);
//
//    }
    public static void course( String classTotal,
            int classTimes,int classLeft,String ptMemberId,SaveCallback SaveCallback)
    {
        AVObject member = new AVObject("CourseStage");
        member.put("classTotal",classTotal);
        member.put("classTimes",classTimes);
//        member.put("firstClassTime",firstClassTime);
        member.put("classLeft", classLeft);
        member.put("ptMemberId", ptMemberId);
        member.saveInBackground(SaveCallback);

    }
    
    public static void logout()
    {
        AVUser.logOut();
    }
    
    public static void createAdvice(String userId, String advice,
            SaveCallback saveCallback)
    {
        AVObject doing = new AVObject("SuggestionByUser");
        doing.put("UserObjectId", userId);
        doing.put("UserSuggestion", advice);
        doing.saveInBackground(saveCallback);
    }
    
}
