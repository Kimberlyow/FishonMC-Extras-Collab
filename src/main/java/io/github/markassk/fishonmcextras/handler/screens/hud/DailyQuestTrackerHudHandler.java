package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.common.Theming;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.DailyQuestHandler;
import io.github.markassk.fishonmcextras.handler.ThemingHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class DailyQuestTrackerHudHandler {
    private static DailyQuestTrackerHudHandler INSTANCE = new DailyQuestTrackerHudHandler();

    public static DailyQuestTrackerHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new DailyQuestTrackerHudHandler();
        }
        return INSTANCE;
    }

    public List<Text> assembleQuestText() {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        List<Text> textList = new ArrayList<>();

        if (ThemingHandler.instance().currentThemeType == Theming.ThemeType.OFF) {
            if(config.dailyQuestTracker.rightAlignment) {
                textList.add(TextHelper.concat(
                        this.getTitle().copy().formatted(Formatting.GRAY),
                        Text.literal(" ◀").formatted(Formatting.GRAY)
                ));
            } else {
                textList.add(TextHelper.concat(
                        Text.literal("▶ ").formatted(Formatting.GRAY),
                        this.getTitle().copy().formatted(Formatting.GRAY)
                ));
            }
        }

        if(DailyQuestHandler.instance() != null && !DailyQuestHandler.instance().quests.isEmpty()) {
            List<DailyQuestHandler.Quest> activeQuests = DailyQuestHandler.instance().quests;

            if(activeQuests != null) {
                activeQuests.forEach(quest -> {
                    if(quest.questDone()) {
                        textList.add(TextHelper.concat(
                                Text.literal("#").formatted(Formatting.GRAY),
                                Text.literal(String.valueOf(quest.slot)).formatted(Formatting.GRAY),
                                Text.literal(": ").formatted(Formatting.GRAY),
                                Text.literal("ꞯᴜᴇѕᴛ ᴅᴏɴᴇ").formatted(Formatting.GREEN, Formatting.BOLD)
                        ));
                    } else if (!quest.isStarted) {
                        textList.add(TextHelper.concat(
                                Text.literal("#").formatted(Formatting.GRAY),
                                Text.literal(String.valueOf(quest.slot)).formatted(Formatting.GRAY),
                                Text.literal(": ").formatted(Formatting.GRAY),
                                Text.literal("ɴᴏᴛ ѕᴛᴀʀᴛᴇᴅ").formatted(Formatting.WHITE)
                        ));
                    } else {
                        textList.add(TextHelper.concat(
                                Text.literal("#").formatted(Formatting.GRAY),
                                Text.literal(String.valueOf(quest.slot)).formatted(Formatting.GRAY),
                                Text.literal(": ").formatted(Formatting.GRAY),
                                Text.literal(quest.goal).formatted(Formatting.WHITE),
                                Text.literal(" (").formatted(Formatting.GRAY),
                                Text.literal(String.valueOf(quest.progress)).formatted(Formatting.YELLOW),
                                Text.literal("/").formatted(Formatting.GRAY),
                                Text.literal(String.valueOf(quest.needed)).formatted(Formatting.WHITE),
                                Text.literal(")").formatted(Formatting.GRAY)
                        ));
                    }
                });
            }
        }

        return textList;
    }

    public Text getTitle() {
        return Text.literal("ᴅᴀɪʟʏ ꞯᴜᴇѕᴛѕ").formatted(Formatting.BOLD);
    }
}
