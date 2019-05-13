package com.example.meiquan.util;

import com.example.meiquan.GlobalData;

public class CalculateCaloryForOneDay {
    double metabolismBaseCalory;//人体基础代谢需要的基本热量

    double totalCalory;


    /*
    1.2：长时间坐在办公室、教室，很少运动或是完全没有运动的人
    1.3：偶尔会运动或散步、逛街、到郊外踏青，每周大约少量运动1-3次的人
    1.5：有持续运动的习惯，或是会上健身房，每周大约运动3-5次的人
    1.7：热爱运动，每周运动6-7次，或是工作量相当大的人
    1.9：工作或生活作息需要大量劳动，相当消耗能量的人
     */

    public double getTotalCalory(){
        double temp = getMetabolismBaseCalory()*GlobalData.sportintensity;
        GlobalData.total_calory = (int)temp;
        return GlobalData.total_calory;
    }

    public double getMetabolismBaseCalory(){
        double metabolismBaseCalory = 0;
        int age = GlobalData.getAge();
        if (GlobalData.sex.compareTo("女生") == 0){
            metabolismBaseCalory = 655 + 9.6 * GlobalData.weight + 1.7 * GlobalData.height - 4.7 * age;
        }else {
            metabolismBaseCalory = 66 + 13.7 * GlobalData.weight + 5.0 * GlobalData.height - 6.8 * age;
        }
        
        return metabolismBaseCalory;
    }
}
