package com.cannawen.dota2timer.configuration.creation;

import com.cannawen.dota2timer.configuration.model.Configuration;

public interface ConfigurationLoader {
    void getConfiguration(ConfigurationLoaderStatusListener listener);

    interface ConfigurationLoaderStatusListener {
        void onLoadConfigurationSuccess(Configuration configuration);

        void onLoadConfigurationFailure(Exception e);
    }
}
