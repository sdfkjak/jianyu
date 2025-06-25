package com.example.mychatapplication;

import android.app.Activity;

import java.util.ArrayList;

public class ActivityCollector {
    private static ActivityCollector instance;
    private ActivityCollector(){}

    public ActivityCollector getInstance() {
        if(instance == null){
            instance = new ActivityCollector();
        }
        return instance;
    }

    private static ArrayList<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void clearAllActivities(){
        for (int i = 0; i < activities.size(); i++) {
            if(!activities.get(i).isFinishing()){
                activities.get(i).finish();
            }
        }
        activities.clear();
    }
}
