package com.veracode.sample.apiwrapper.utils;

import com.veracode.apiwrapper.AbstractAPIWrapper;

/**
 * The factory to create API Wrappers
 */
public class WrapperFactory {
    private final String apiID;
    private final String apiKey;

    public WrapperFactory(String apiID, String apiKey) {
        this.apiID = apiID;
        this.apiKey = apiKey;
    }

    public static final WrapperFactory newInstance(String apiID, String apiKey) {
        return new WrapperFactory(apiID, apiKey);
    }

    public final <T extends AbstractAPIWrapper> T createWrapper(Class<T> clazz) {
        T wrapper = null;
        try {
            wrapper = clazz.newInstance();
            wrapper.setUpApiCredentials(apiID, apiKey);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot create wrapper.", e);
        }
        return wrapper;
    }
}
