package com.warfarin_app.data;

/**
 * Created by Coming on 8/9/15.
 */
import android.app.Activity;
import android.content.SharedPreferences;


public class Patient {


//    public final static String PRE_USER_NAME = "PRE_USER_NAME";
//    public final static String PRE_IS_MALE   = "PRE_IS_MALE";
//    public final static String PRE_BIRTHDAY   = "PRE_BIRTHDAY";
//    public final static String PRE_DOCTOR   = "PRE_DOCTOR";
//    public final static String PRE_IS_WARFARIN   = "PRE_IS_WARFARIN";


    private long id = -1;
    private String name = "";
    private boolean gender = true;  // true: male, false: female
    private String birthday = "";
    private String doctor = "";
    private boolean isWarfarin = false;
    private String bluedev_name = "";
    private String bluedev_address = "";

    SharedPreferences sharedPref;
    Activity activity = null;

    public Patient()
    {
//        activity = act;
//        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

    }

//    public void load()
//    {
//        name = sharedPref.getString(PRE_USER_NAME, "user_name");
//
//        Log.d("app", "Load, user_name: " + name);
//    }
//
//    public void save()
//    {
////        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(PRE_USER_NAME, name);
//
//        editor.commit();
//
//
//        Log.d("app", "Save, user_name: " + name);
//
//    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;

    }

    public String getDoctor()
    {
        return this.doctor;
    }

    public void setDoctor(String doctor)
    {
        this.doctor = doctor;

    }

    public String getBirthday()
    {
        return this.birthday;
    }

    public void setBirthday(String d)
    {
        this.birthday = d;

    }

    public boolean getIsWarfarin()
    {
        return this.isWarfarin;
    }

    public void setIsWarfarin(boolean isWarfarin)
    {
        this.isWarfarin = isWarfarin;

    }

    public boolean getGender()
    {
        return this.gender;
    }

    public void setGender(boolean gender)
    {
        this.gender = gender;
    }

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setBlueDevName(String name)
    {
        this.bluedev_name = name;
    }

    public String getBlueDevName()
    {
        return this.bluedev_name;
    }

    public void setBlueDevAddress(String address)
    {
        this.bluedev_address = address;
    }

    public String getBlueDevAddress()
    {
        return this.bluedev_address;
    }

}
