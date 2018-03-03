package com.cannawen.dota2timer.configuration.creation;

import android.content.Context;

import com.cannawen.dota2timer.configuration.model.Configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LocalConfigurationLoader implements ConfigurationLoader {
    private Context context;

    @Override
    public void getConfiguration(ConfigurationLoaderStatusListener listener) {
        try {
            InputStream inputStream = context.getAssets().open("configuration.yml");
            Configuration config = new Yaml().loadAs(inputStream, Configuration.class);
            listener.onLoadConfigurationSuccess(config);
        } catch (IOException e) {
            listener.onLoadConfigurationFailure(e);
        }
    }
}
