package com.cannawen.dota2timer.configuration.loading;

import com.cannawen.dota2timer.configuration.Configuration;

public interface ConfigurationLoaderListener {
    void onSuccess(Configuration configuration);

    void onFailure(Exception e);
}
