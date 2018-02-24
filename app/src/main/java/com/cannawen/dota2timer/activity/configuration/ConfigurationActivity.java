package com.cannawen.dota2timer.activity.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.Configuration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfigurationActivity extends Activity {

    private static final String INTENT_CONFIGURATION_KEY = "INTENT_CONFIGURATION_KEY";

    static public Intent createActivityIntent(Context context, Configuration configuration) {
        Intent intent = new Intent(context, ConfigurationActivity.class);
        intent.putExtra(INTENT_CONFIGURATION_KEY, configuration);
        return intent;
    }

    static public Configuration deserializeConfigurationFromIntent(Intent intent) {
        return (Configuration) intent.getSerializableExtra(INTENT_CONFIGURATION_KEY);
    }

    @BindView(R.id.activity_configuration_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        ConfigurationAdapter adapter = new ConfigurationAdapter(getApplicationContext(), deserializeConfigurationFromIntent(getIntent()));
        recyclerView.setAdapter(adapter);
    }
}
