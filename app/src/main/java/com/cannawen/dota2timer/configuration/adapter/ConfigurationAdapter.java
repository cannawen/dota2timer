package com.cannawen.dota2timer.configuration.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.model.Configuration;
import com.cannawen.dota2timer.configuration.model.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationAdapter.ConfigurationViewHolder> {

    private Context context;
    private Configuration configuration;
    private boolean detailed;

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
        return configuration.getEvents().size();
    }

    class ConfigurationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cell_edit_event_name)
        TextView nameText;
        @BindView(R.id.cell_edit_event_initial)
        TextView initialText;
        @BindView(R.id.cell_edit_event_period)
        TextView periodText;
        @BindView(R.id.cell_edit_event_notice)
        TextView noticeText;
        @BindView(R.id.cell_edit_event_enabled_box)
        CheckBox enabledCheckBox;

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
