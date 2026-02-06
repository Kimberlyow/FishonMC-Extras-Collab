package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerFriendHUDConfig {
        public static class FriendTracker {
        public boolean showFriendNearby = true;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int fontSize = 8;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        public boolean notifyFriendOnJoin = true;
        public boolean notifyFriendOnLeave = true;
        public boolean showFriendTag = true;
        @ConfigEntry.Gui.Tooltip
        public boolean isPrefix = true;
    }
}
