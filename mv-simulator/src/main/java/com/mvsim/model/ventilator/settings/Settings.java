package com.mvsim.model.ventilator.settings;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mvsim.model.Setting;

/**
 * Represents the superclass of the settings for all ventilation modes.
 */
public abstract class Settings implements Iterable<Setting> {
    private final Map<String, Setting> settingsMap = new LinkedHashMap<>();
    private Trigger trigger;

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
        if (trigger instanceof Setting) {
            Setting triggerSetting = (Setting) trigger;
            addSetting(triggerSetting.getName(), triggerSetting);
        } else {
            settingsMap.remove(Trigger.NAME);
        }
    }

    protected Settings(Map<String, Setting> modeSpecificSettings) {
        addSetting(FiO2.NAME, new FiO2());
        addSetting(Peep.NAME, new Peep());
        this.trigger = new NoTrigger();

        if (modeSpecificSettings != null) {
            settingsMap.putAll(modeSpecificSettings);
        }
    }

    protected void addSetting(String key, Setting setting) {
        settingsMap.put(key, setting);
    }

    /**
     * A single generic method to retrieve any setting by its key.
     * This replaces the need for individual getter methods.
     * @param key The string key for the setting (e.g., FiO2.NAME).
     * @param <T> The type of the Setting subclass to return.
     * @return The setting object, cast to the specified type.
     */
    @SuppressWarnings("unchecked")
    public <T extends Setting> T getSetting(String key) {
        return (T) settingsMap.get(key);
    }

    @Override
    public Iterator<Setting> iterator() {
        return settingsMap.values().iterator();
    }
}
