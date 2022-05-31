package com.cucumber;

import aquality.selenium.core.logging.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to hold and pass data between test methods
 */
public class ScenarioContext {
    private static final Map<String, Object> scenario = new HashMap<>();

    private ScenarioContext() {}

    public static void setContext(Context key, Object value) {
        scenario.put(key.toString(), value);
    }

    public static String getContext(Context key) {
        String context = null;
        try {
            context = scenario.get(key.toString()).toString();
        } catch(NullPointerException ex) {
            Logger.getInstance().error(ex.getMessage());
        }
        return context;
    }
}
