package com.cannawen.dota2timer.configuration.loading;

import com.cannawen.dota2timer.configuration.Configuration;

public interface ConfigurationLoader {
    void getConfiguration(ConfigurationLoaderStatusListener listener);

    interface ConfigurationLoaderStatusListener {
        void onLoadConfigurationSuccess(Configuration configuration);

        void onLoadConfigurationFailure(Exception e);
    }
}
