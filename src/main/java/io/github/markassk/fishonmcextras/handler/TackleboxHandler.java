package io.github.markassk.fishonmcextras.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;

public class TackleboxHandler {
    private static final TackleboxHandler INSTANCE = new TackleboxHandler();
    private static final float LOCK_ITEM_MODEL_DATA = 47.0f;

    public boolean isLocked = false;

    public static TackleboxHandler instance() { return INSTANCE; }

    public void loadFromProfile() {
        if (ProfileDataHandler.instance().isDataLoaded) {
            this.isLocked = ProfileDataHandler.instance().profileData.tackleboxLocked;
        }
    }

    public void setLocked(boolean value) {
        this.isLocked = value;
        if (ProfileDataHandler.instance().isDataLoaded) {
            ProfileDataHandler.instance().profileData.tackleboxLocked = value;
        }
    }

    private boolean isLockItem(ItemStack stack) {
        CustomModelDataComponent cmd = stack.get(DataComponentTypes.CUSTOM_MODEL_DATA);
        if (cmd == null || cmd.floats().isEmpty()) return false;
        return Math.abs(cmd.floats().get(0) - LOCK_ITEM_MODEL_DATA) < 0.01f;
    }

    public void onScreenOpen(MinecraftClient client) {
        if (client.player == null || client.currentScreen == null) return;

        String title = client.currentScreen.getTitle().getString();
        if (!title.contains("Tacklebox") && !title.contains("Tackle Box")) return;

        boolean detectedLocked = false;
        for (int i = 0; i < client.player.currentScreenHandler.slots.size(); i++) {
            ItemStack stack = client.player.currentScreenHandler.getSlot(i).getStack();
            if (!isLockItem(stack)) continue;

            NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
            if (customData != null) {
                detectedLocked = customData.copyNbt().getInt("disableBait") == 1;
            }
            break;
        }
        setLocked(detectedLocked);
    }
}
