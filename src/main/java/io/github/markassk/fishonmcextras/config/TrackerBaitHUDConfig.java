package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerBaitHUDConfig {
    public static class BaitTracker {
        public boolean showBaitHud = true;
        public boolean showBaitWarningHUD = true;
        @ConfigEntry.Gui.Tooltip
        public boolean showLowBaitWarningHUD = true;
        @ConfigEntry.Gui.Tooltip
        public int lowBaitThreshold = 10;
        public boolean rightAlignment = false;
        @ConfigEntry.Gui.Tooltip
        public boolean calculateLures = true;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudY = 100;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 300)
        public int offsetFromMiddle = 116;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int fontSize = 8;
    }
}
