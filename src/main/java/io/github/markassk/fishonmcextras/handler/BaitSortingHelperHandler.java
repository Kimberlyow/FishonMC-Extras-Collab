package io.github.markassk.fishonmcextras.handler;

import java.util.HashMap;
import java.util.Map;

import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.util.ColorHelper;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;

public class BaitSortingHelperHandler {
    private static BaitSortingHelperHandler INSTANCE = new BaitSortingHelperHandler();

    public boolean toggle = false;

    public static BaitSortingHelperHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new BaitSortingHelperHandler();
        }
        return INSTANCE;
    }

    public void renderItemMarker(DrawContext drawContext, ItemStack itemStack, int x, int y) {
        boolean showOnlyWhilePressingKeybind = FishOnMCExtrasConfig
                .getConfig().baitSortingHelperVisibility.showOnlyWhilePressingKeybind;

        if (!FOMCItem.isFOMCItem(itemStack)) {
            return;
        }

        boolean isActive = showOnlyWhilePressingKeybind
                ? KeybindHandler.instance().visualizeBaitSorting
                : BaitSortingHelperHandler.instance().toggle;

        if (!isActive) {
            return;
        }

        if (!isBait(itemStack)) {
            return;
        }

        Map<String, Integer> baitCounts = getCurBaits();
        String baitKey = getBaitKey(itemStack);

        if (baitCounts.getOrDefault(baitKey, 0) > 1) {
            int alphaInt = (int) (0.6f * 255f) << 24;
            int rgb = ColorHelper.getClrFromString(baitKey);

            drawContext.getMatrices().push();
            try {
                drawContext.getMatrices().translate(0, 0, 100);
                drawContext.fill(x, y, x + 16, y + 16,
                        alphaInt | rgb);
            } finally {
                drawContext.getMatrices().pop();
            }
        }
    }

    private static boolean isBait(ItemStack itemStack) {
        NbtCompound data = ItemStackHelper.getNbt(itemStack);
        if (data == null) {
            return false;
        }
        if (!data.contains("type")) {
            return false;
        }
        return "bait".equalsIgnoreCase(data.getString("type"));
    }

    private static String getBaitKey(ItemStack itemStack) {
        NbtCompound data = ItemStackHelper.getNbt(itemStack);
        if (data != null && data.contains("name")) {
            String nbtName = data.getString("name");
            if (nbtName != null && !nbtName.isBlank()) {
                return nbtName.toLowerCase();
            }
        }
        return itemStack.getName().getString().toLowerCase();
    }

    private static Map<String, Integer> getCurBaits() {
        Map<String, Integer> counts = new HashMap<>();

        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (!(screen instanceof HandledScreen<?> handledScreen)) {
            return counts;
        }

        for (var slot : handledScreen.getScreenHandler().slots) {
            ItemStack stack = slot.getStack();
            if (stack == null || stack.isEmpty() || !FOMCItem.isFOMCItem(stack) || !isBait(stack)) {
                continue;
            }

            String key = getBaitKey(stack);
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }

        return counts;
    }
}
