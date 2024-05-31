package com.nemanja02.projekat.repositories.activity;

import com.nemanja02.projekat.entities.Activity;
import com.nemanja02.projekat.entities.Comment;

import java.util.List;

public interface ActivityRepository {

    public Activity addActivity(Activity activity);
    public List<Activity> getActivities();
    public void deleteActivity(Integer id);
    public Activity getActivityByID(Integer id);
}
