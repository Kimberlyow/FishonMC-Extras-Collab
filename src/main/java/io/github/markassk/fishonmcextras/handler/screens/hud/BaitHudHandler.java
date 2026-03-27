package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.FOMC.Types.Bait;
import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import io.github.markassk.fishonmcextras.FOMC.Types.Lure;
import io.github.markassk.fishonmcextras.handler.FishingRodHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class BaitHudHandler {
    private static BaitHudHandler INSTANCE = new BaitHudHandler();

    public static BaitHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new BaitHudHandler();
        }
        return INSTANCE;
    }

    public Text assembleBaitText() {
        if (FishingRodHandler.instance().isTackleboxDisabled(MinecraftClient.getInstance())) {
            return Text.literal("");
        }

        var fishingRod = FishingRodHandler.instance().fishingRod;
        if (fishingRod == null || fishingRod.tacklebox.isEmpty()) {
            return Text.literal("");
        }

        if (fishingRod.tacklebox.getFirst() instanceof Lure firstLure
                && FishOnMCExtrasConfig.getConfig().baitTracker.calculateLures) {

            int lureQty = firstLure.calculateLures(fishingRod.tacklebox);

            if (lureQty > 0) {
                return TextHelper.concat(
                        Text.literal(TextHelper.upperCaseAllFirstCharacter(firstLure.name)).formatted(Formatting.WHITE),
                        Text.literal(": ").formatted(Formatting.GRAY),
                        Text.literal(String.valueOf(lureQty)).formatted(Formatting.WHITE),
                        Text.literal("x").formatted(Formatting.GRAY));
            }
        }

        return TextHelper.concat(
                fishingRod.tacklebox.getFirst() instanceof Bait bait
                        ? Text.literal(TextHelper.upperCaseAllFirstCharacter(bait.name)).formatted(Formatting.WHITE)
                        : fishingRod.tacklebox.getFirst() instanceof Lure lure ? Text
                                .literal(TextHelper.upperCaseAllFirstCharacter(lure.name)).formatted(Formatting.WHITE)
                                : Text.empty(),
                Text.literal(": ").formatted(Formatting.GRAY),
                fishingRod.tacklebox.getFirst() instanceof Bait bait
                        ? Text.literal(String.valueOf(bait.counter)).formatted(Formatting.WHITE)
                        : fishingRod.tacklebox.getFirst() instanceof Lure lure
                                ? Text.literal(String.valueOf(lure.counter)).formatted(Formatting.WHITE)
                                : Text.empty(),
                Text.literal("x").formatted(Formatting.GRAY));
    }

    public CustomModelDataComponent getModelData() {
        if (FishingRodHandler.instance().isTackleboxDisabled(MinecraftClient.getInstance())) {
            return CustomModelDataComponent.DEFAULT;
        }

        var fishingRod = FishingRodHandler.instance().fishingRod;
        if (fishingRod == null || fishingRod.tacklebox.isEmpty()) {
            return CustomModelDataComponent.DEFAULT;
        }
        return fishingRod.tacklebox.getFirst() instanceof Bait bait ? bait.customModelData
                : fishingRod.tacklebox.getFirst() instanceof Lure lure ? lure.customModelData
                                : CustomModelDataComponent.DEFAULT;
    }
}
