package com.cannawen.dota2timer.configuration.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.model.Configuration;
import com.cannawen.dota2timer.configuration.model.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationAdapter.ConfigurationViewHolder> {

    @NonNull
    private Context context;
    @NonNull
    private boolean detailed;
    @Nullable
    private Configuration configuration;

    @Override
    public ConfigurationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_edit_event, parent, false);
        return new ConfigurationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConfigurationViewHolder holder, int position) {
        holder.configureWithEvent(configuration.getEvents().get(position), position);
    }

    @Override
    public int getItemCount() {
        if (configuration == null) {
            return 0;
        } else {
            return configuration.getEvents().size();
        }
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        notifyDataSetChanged();
    }

    class ConfigurationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cell_edit_event_enabled_box)
        CheckBox enabledCheckBox;
        @BindView(R.id.cell_edit_event_name)
        EditText nameText;
        @BindView(R.id.cell_edit_event_initial)
        EditText initialText;
        @BindView(R.id.cell_edit_event_period)
        EditText periodText;
        @BindView(R.id.cell_edit_event_notice)
        EditText noticeText;

        ConfigurationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void configureWithEvent(Event event, int position) {
            nameText.setText(event.getName());
            if (detailed) {
                initialText.setText(String.valueOf(event.getTime_initial()));
                periodText.setText(String.valueOf(event.getTime_repeat()));
                noticeText.setText(String.valueOf(event.getTime_advance_notice()));
            } else {
                nameText.setFocusableInTouchMode(false);
                initialText.setVisibility(View.GONE);
                periodText.setVisibility(View.GONE);
                noticeText.setVisibility(View.GONE);
            }

            enabledCheckBox.setChecked(event.isEnabled());
            enabledCheckBox.setTag(position);
            enabledCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int index = (int) buttonView.getTag();
                configuration.getEvents().get(index).setEnabled(isChecked);
            });
        }
    }
}
