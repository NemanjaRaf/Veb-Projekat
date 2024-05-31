package com.nemanja02.projekat.services;

import com.nemanja02.projekat.entities.Activity;
import com.nemanja02.projekat.repositories.activity.ActivityRepository;

import javax.inject.Inject;
import java.util.List;

public class ActivityService {

    public ActivityService() {
    }

    @Inject
    private ActivityRepository activityRepository;

    public Activity addActivity(Activity activity) {
        return this.activityRepository.addActivity(activity);
    }
    public List<Activity> getActivities() {
        return this.activityRepository.getActivities();
    }
    public void deleteActivity(Integer id) {
        this.activityRepository.deleteActivity(id);
    }
    public Activity getActivityByID(Integer id) {
        return this.activityRepository.getActivityByID(id);
    }

}
