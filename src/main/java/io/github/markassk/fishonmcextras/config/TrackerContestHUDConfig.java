package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerContestHUDConfig {
    public enum ContestStatsDisplay {
        ALWAYS("Always"),
        AT_LOCATION("At Contest Location"),
        NEVER("Never");
        
        private final String displayName;
        
        ContestStatsDisplay(String displayName) {
            this.displayName = displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    public static class ContestTracker {
        public boolean showContest = true;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public ContestStatsDisplay contestStatsDisplay = ContestStatsDisplay.AT_LOCATION;
        @ConfigEntry.Gui.CollapsibleObject
        public CompactDisplayOptions compact = new CompactDisplayOptions();
        public static class CompactDisplayOptions {
            @ConfigEntry.Gui.Tooltip
            public boolean mergeTypeAndLocation = false;
            @ConfigEntry.Gui.Tooltip
            public boolean hideLocationWarning = false;
            @ConfigEntry.Gui.Tooltip
            public boolean removeExtraSpacing = false;
            @ConfigEntry.Gui.Tooltip
            public boolean combineRankLine = false;
        }
        @ConfigEntry.Gui.Tooltip
        public boolean refreshOnContestPB = true;
        @ConfigEntry.Gui.Tooltip
        public boolean recieveLocalPBs = true;
        @ConfigEntry.Gui.Tooltip
        public boolean suppressServerMessages = true;
        @ConfigEntry.Gui.Tooltip
        public boolean useOldContestHUD = false;
        @ConfigEntry.Gui.Tooltip
        public boolean showFullContest = true;
        public boolean rightAlignment = false;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(max = 20, min = 2)
        public int fontSize = 8;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudX = 0;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudY = 0;
        
        /**
         * Returns true when contest stats display is not set to NEVER
         * This replaces the old showFullContest field
         */
        public boolean shouldShowFullContest() {
            return contestStatsDisplay != ContestStatsDisplay.NEVER;
        }
    }
}
