package com.cannawen.dota2timer.configuration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.model.Configuration;
import com.cannawen.dota2timer.configuration.adapter.ConfigurationAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigurationActivity extends Activity {

    private static final String INTENT_CONFIGURATION_KEY = "INTENT_CONFIGURATION_KEY";

    @BindView(R.id.activity_configuration_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.cell_edit_event_enabled_box)
    View checkBoxView;

    @BindView(R.id.cell_edit_event_name)
    TextView eventNameDescription;

    @BindView(R.id.cell_edit_event_initial)
    TextView eventInitialDescription;

    @BindView(R.id.cell_edit_event_period)
    TextView eventPeriodDescription;

    @BindView(R.id.cell_edit_event_notice)
    TextView eventNoticeDescription;

    private Configuration configuration;

    static public Intent configureIntent(Intent intent, Configuration configuration) {
        intent.putExtra(INTENT_CONFIGURATION_KEY, configuration);
        return intent;
    }

    static public Configuration deserializeConfigurationFromIntent(Intent intent) {
        return (Configuration) intent.getSerializableExtra(INTENT_CONFIGURATION_KEY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        ButterKnife.bind(this);

        checkBoxView.setVisibility(View.INVISIBLE);
        eventNameDescription.setText(R.string.edit_configuration_description_event_name);
        eventInitialDescription.setText(R.string.edit_configuration_description_event_initial);
        eventPeriodDescription.setText(R.string.edit_configuration_description_event_period);
        eventNoticeDescription.setText(R.string.edit_configuration_description_event_notice);


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        configuration = deserializeConfigurationFromIntent(getIntent());
        ConfigurationAdapter adapter = new ConfigurationAdapter(getApplicationContext(), true, configuration);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.activity_configuration_save)
    public void save() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(INTENT_CONFIGURATION_KEY, configuration);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @OnClick(R.id.activity_configuration_cancel)
    public void cancel() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
