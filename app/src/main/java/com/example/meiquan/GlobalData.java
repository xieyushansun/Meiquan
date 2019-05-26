package com.example.meiquan;

import android.widget.ImageView;

import com.example.meiquan.entity.FoodCalory;
import com.example.meiquan.entity.SportCalory;


import java.util.Calendar;
import java.util.List;


public class GlobalData{
    public static String phone = "";
    public static String nickname = "";
    public static String password = "";
    public static int height = 0;  //cm
    public static int weight = 0;  //kg
    public static String sex = "";
    public static String type = ""; //学生或工作者
    public static double sportintensity = 1.2;
    //出生年月日
    public static int birthyear = 0;
    public static int birthmonth = 0;
    public static int birthday = 0;
    //省市
    public static String province;
    public static String city;

    public static List<SportCalory> sportCaloryList;
    public static List<FoodCalory> foodCaloryList;

    public static int total_calory = 0;

    public static int total_morning_food = 0;  //早上
    public static int total_aftermorning_food = 0;  //上午
    public static int total_noon_food = 0;  //中午
    public static int total_afternoon_food = 0;  //下午
    public static int total_night_food = 0;  //晚上
    public static int total_afternight_food = 0;  //深夜
    public static int total_food = 0;

    public static int total_morning_sport = 0;  //早上
    public static int total_aftermorning_sport = 0;  //上午
    public static int total_noon_sport = 0;  //中午
    public static int total_afternoon_sport = 0;  //下午
    public static int total_night_sport = 0;  //晚上
    public static int total_afternight_sport = 0;  //深夜
    public static int total_sport = 0;

    public static String headimage_url = "";

    public static int flag_firstregisterOrresetInfo = 0;

    public static int getAge(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int age = year-birthyear;

        return age;
    }
}
