package com.cannawen.dota2timer.activity;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

public class ActivityTestHelper {
    private Activity activity;

    public ActivityTestHelper(Activity activity) {
        this.activity = activity;
    }

    public Boolean isVisible(@IdRes int id) {
        return activity.findViewById(id).getVisibility() == View.VISIBLE;
    }

    public void click(@IdRes int id) {
        activity.findViewById(id).callOnClick();
    }
}
