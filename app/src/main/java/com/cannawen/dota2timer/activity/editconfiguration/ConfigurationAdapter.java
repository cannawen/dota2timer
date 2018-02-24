package com.cannawen.dota2timer.activity.editconfiguration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationAdapter.ConfigurationViewHolder> {

    private Context context;
    private Configuration configuration;

    @Override
    public ConfigurationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_edit_event, parent, false);
        return new ConfigurationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConfigurationViewHolder holder, int position) {
        holder.configureWithEvent(configuration.getEvents().get(position));
    }

    @Override
    public int getItemCount() {
        return configuration.getEvents().size();
    }

    class ConfigurationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cell_edit_event_description_text)
        TextView descriptionText;
        @BindView(R.id.cell_edit_event_enabled_box)
        CheckBox enabledCheckBox;

        ConfigurationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void configureWithEvent(Event event) {
            descriptionText.setText(event.getName());
            enabledCheckBox.setChecked(true);
        }
    }
}
