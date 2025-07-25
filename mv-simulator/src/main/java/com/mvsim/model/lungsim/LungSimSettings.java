package com.mvsim.model.lungsim;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mvsim.model.ventilator.settings.NewSetting;

public class LungSimSettings implements Iterable<Entry<LungSimSetting, Number>> {

    private Map<LungSimSetting, Number> settingsMap = new LinkedHashMap<>();

    protected LungSimSettings() {
        settingsMap.put(LungSimSetting.COMPLIANCE, LungSimSetting.COMPLIANCE.getDefaultValue());
        settingsMap.put(LungSimSetting.RESISTANCE, LungSimSetting.RESISTANCE.getDefaultValue());
    }

    public Number getSetting(NewSetting setting) {
        return settingsMap.get(setting);
    }

    public void setSetting(LungSimSetting setting, Number value) {
        settingsMap.put(setting, value);
    }

    @Override
    public Iterator<Entry<LungSimSetting, Number>> iterator() {
        return settingsMap.entrySet().iterator();
    }


}
