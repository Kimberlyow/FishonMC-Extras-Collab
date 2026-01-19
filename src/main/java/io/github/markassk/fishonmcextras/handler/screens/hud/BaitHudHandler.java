package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.FOMC.Types.Bait;
import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import io.github.markassk.fishonmcextras.FOMC.Types.Lure;
import io.github.markassk.fishonmcextras.handler.FishingRodHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;

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
        List<FOMCItem> tacklebox = FishingRodHandler.instance().fishingRod.tacklebox;
        if (tacklebox.isEmpty()) {
            return Text.literal("");
        }

        if (tacklebox.getFirst() instanceof Lure firstLure
                && FishOnMCExtrasConfig.getConfig().baitTracker.calculateLures) {

            int lureQty = firstLure.calculateLures(tacklebox);

            if (lureQty > 0) {
                return TextHelper.concat(
                        Text.literal(TextHelper.upperCaseAllFirstCharacter(firstLure.name)).formatted(Formatting.WHITE),
                        Text.literal(": ").formatted(Formatting.GRAY),
                        Text.literal(String.valueOf(lureQty)).formatted(Formatting.WHITE),
                        Text.literal("x").formatted(Formatting.GRAY));
            }
        }

        return TextHelper.concat(
                tacklebox.getFirst() instanceof Bait bait
                        ? Text.literal(TextHelper.upperCaseAllFirstCharacter(bait.name)).formatted(Formatting.WHITE)
                        : tacklebox.getFirst() instanceof Lure lure ? Text
                                .literal(TextHelper.upperCaseAllFirstCharacter(lure.name)).formatted(Formatting.WHITE)
                                : Text.empty(),
                Text.literal(": ").formatted(Formatting.GRAY),
                tacklebox.getFirst() instanceof Bait bait
                        ? Text.literal(String.valueOf(bait.counter)).formatted(Formatting.WHITE)
                        : tacklebox.getFirst() instanceof Lure lure
                                ? Text.literal(String.valueOf(lure.counter)).formatted(Formatting.WHITE)
                                : Text.empty(),
                Text.literal("x").formatted(Formatting.GRAY));
    }

    public CustomModelDataComponent getModelData() {
        List<FOMCItem> tacklebox = FishingRodHandler.instance().fishingRod.tacklebox;
        return !tacklebox.isEmpty()
                ? tacklebox.getFirst() instanceof Bait bait ? bait.customModelData
                : tacklebox.getFirst() instanceof Lure lure
                                ? lure.customModelData
                                : CustomModelDataComponent.DEFAULT
                : CustomModelDataComponent.DEFAULT;
    }
}
