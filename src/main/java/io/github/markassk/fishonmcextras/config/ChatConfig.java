package io.github.markassk.fishonmcextras.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ChatConfig {
    public static class ChatFilter {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = false;
    }
}