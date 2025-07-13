package com.mvsim.model.ventilator.mode;

import com.mvsim.model.ventilator.settings.InspiratoryTime;
import com.mvsim.model.ventilator.settings.RespiratoryRate;
import com.mvsim.model.ventilator.settings.Setting;
import com.mvsim.model.ventilator.settings.Settings;
import com.mvsim.model.ventilator.settings.TidalVolume;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents the settings specific to VC-CMVs.
 */
public class VcCmvSetpointSettings extends Settings {

    public VcCmvSetpointSettings() {
        super(createModeSpecificSettings());
    }

    private static Map<String, Setting> createModeSpecificSettings() {
        Map<String, Setting> specificSettings = new LinkedHashMap<>();
        specificSettings.put(RespiratoryRate.NAME, new RespiratoryRate(16));
        specificSettings.put(TidalVolume.NAME, new TidalVolume(300));
        specificSettings.put(InspiratoryTime.NAME, new InspiratoryTime(0.9));
        return specificSettings;
    }
}
