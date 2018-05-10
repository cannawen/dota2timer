package com.cannawen.dota2timer.configuration.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.model.Configuration;
import com.cannawen.dota2timer.configuration.model.Event;
import com.cannawen.dota2timer.utility.TimeFormattingUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

        ConfigurationTextWatcher nameChangeListener;
        ConfigurationTextWatcher initialChangeListener;
        ConfigurationTextWatcher periodChangeListener;
        ConfigurationTextWatcher noticeChangeListener;
        ConfigurationCheckboxListener checkboxListener;

        ConfigurationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            nameChangeListener = new ConfigurationTextWatcher(true, false, false, false);
            nameText.addTextChangedListener(nameChangeListener);
            initialChangeListener = new ConfigurationTextWatcher(false, true, false, false);
            initialText.addTextChangedListener(initialChangeListener);
            periodChangeListener = new ConfigurationTextWatcher(false, false, true, false);
            periodText.addTextChangedListener(periodChangeListener);
            noticeChangeListener = new ConfigurationTextWatcher(false, false, false, true);
            noticeText.addTextChangedListener(noticeChangeListener);

            checkboxListener = new ConfigurationCheckboxListener();
            enabledCheckBox.setOnCheckedChangeListener(checkboxListener);
        }

        void configureWithEvent(Event event, int position) {
            nameChangeListener.setPosition(position);
            initialChangeListener.setPosition(position);
            periodChangeListener.setPosition(position);
            noticeChangeListener.setPosition(position);
            checkboxListener.setPosition(position);

            nameText.setText(event.getName());
            if (detailed) {
                initialText.setText(TimeFormattingUtility.parseTimeSecondsToString(event.getTime_initial()));
                periodText.setText(TimeFormattingUtility.parseTimeSecondsToString(event.getTime_repeat()));
                noticeText.setText(TimeFormattingUtility.parseTimeSecondsToString(event.getTime_advance_notice()));
            } else {
                nameText.setFocusableInTouchMode(false);
                initialText.setVisibility(View.GONE);
                periodText.setVisibility(View.GONE);
                noticeText.setVisibility(View.GONE);
            }

            enabledCheckBox.setChecked(event.isEnabled());
        }
    }

    class ConfigurationCheckboxListener implements CompoundButton.OnCheckedChangeListener {
        @Setter
        private int position;

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            configuration.getEvents().get(position).setEnabled(isChecked);
        }
    }

    class ConfigurationTextWatcher implements TextWatcher {
        @Setter
        private int position;
        private boolean name;
        private boolean initial;
        private boolean repeat;
        private boolean notice;

        public ConfigurationTextWatcher(boolean name, boolean initial, boolean repeat, boolean notice) {
            this.name = name;
            this.initial = initial;
            this.repeat = repeat;
            this.notice = notice;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Event event = configuration.getEvents().get(position);
            String string = s.toString();
            if (name) {
                event.setName(string);
            } else {
                int time = s.length() == 0 ? 0 : TimeFormattingUtility.parseTimeStringToSeconds(string);
                if (initial) {
                    event.setTime_initial(time);
                }
                if (repeat) {
                    event.setTime_repeat(time);
                }
                if (notice) {
                    event.setTime_advance_notice(time);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
