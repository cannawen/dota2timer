package com.cannawen.dota2timer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.Configuration;

public class EditConfigurationActivity extends Activity {

    private static final String INTENT_CONFIGURATION_KEY = "INTENT_CONFIGURATION_KEY";

    static public Intent createActivityIntent(Context context, Configuration configuration) {
        Intent intent = new Intent(context, EditConfigurationActivity.class);
        intent.putExtra(INTENT_CONFIGURATION_KEY, configuration);
        return intent;
    }

    static public Configuration deserializeConfigurationFromIntent(Intent intent) {
        return (Configuration) intent.getSerializableExtra(INTENT_CONFIGURATION_KEY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_configuration);
    }
}
