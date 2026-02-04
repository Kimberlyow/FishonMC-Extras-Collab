package io.github.markassk.fishonmcextras.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.FOMC.Types.Armor;
import io.github.markassk.fishonmcextras.mixin.HandledScreenAccessor;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class LockRollHandler {
    private static LockRollHandler INSTANCE = new LockRollHandler();
    private boolean hasInitialized = false;

    public boolean armorRollsMenuState = false;
    private Integer currentArmorKey = null;

    public static LockRollHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new LockRollHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if (armorRollsMenuState && minecraftClient.player != null) {
            this.currentArmorKey = findCurrentArmorKey(minecraftClient);

            if (minecraftClient.player.currentScreenHandler != null) {
                for (Slot slot : minecraftClient.player.currentScreenHandler.slots) {
                    if (slot.inventory == minecraftClient.player.getInventory()) {
                        continue;
                    }

                    ItemStack stack = slot.getStack();
                    if (isActiveBonusSlot(stack)) {
                        boolean locked = isLockedArmorRollSlot(minecraftClient, slot);
                        updateLore(stack, locked);
                    }
                }
            }
        } 

        if (!hasInitialized && LoadingHandler.instance().isLoadingDone && ProfileDataHandler.instance().isDataLoaded) {
            hasInitialized = true;
            if (ProfileDataHandler.instance().profileData.lockedArmorRolls == null) {
                ProfileDataHandler.instance().profileData.lockedArmorRolls = new HashMap<>();
            } else {
                ProfileDataHandler.instance().profileData.lockedArmorRolls.values().removeIf(list -> list == null || list.isEmpty());
            }
        }
    }

    public void toggleArmorRollLock(MinecraftClient minecraftClient) {
        if (!armorRollsMenuState || minecraftClient.player == null) {
            return;
        }

        Slot focusedSlot = getFocusedSlot(minecraftClient);
        if (focusedSlot == null) {
            return;
        }

        ItemStack focusedStack = focusedSlot.getStack();
        if (!isActiveBonusSlot(focusedStack)) {
            return;
        }

        Integer armorKey = findCurrentArmorKey(minecraftClient);
        if (armorKey == null) {
            return;
        }

        int slotIndex = getScreenSlotIndex(minecraftClient, focusedSlot);
        if (slotIndex < 0) {
            return;
        }

        String slotKey = String.valueOf(slotIndex);
        Map<Integer, List<String>> lockedArmorRolls = ProfileDataHandler.instance().profileData.lockedArmorRolls;
        List<String> lockedSlots = lockedArmorRolls.computeIfAbsent(armorKey, key -> new ArrayList<>());
        if (lockedSlots.contains(slotKey)) {
            lockedSlots.remove(slotKey);
            if (lockedSlots.isEmpty()) {
                lockedArmorRolls.remove(armorKey);
            }
            minecraftClient.player.playSound(SoundEvents.BLOCK_CHERRY_WOOD_TRAPDOOR_OPEN, 1.0f, 1.0f);
        } else {
            lockedSlots.add(slotKey);
            minecraftClient.player.playSound(SoundEvents.BLOCK_CHEST_LOCKED, 1.0f, 1.0f);
        }

        ProfileDataHandler.instance().saveStats();
    }

    public void renderLockedArmorRollMarker(DrawContext drawContext, Slot slot) {
        if (!armorRollsMenuState) {
            return;
        }

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.player == null
                || slot.inventory == minecraftClient.player.getInventory()) {
            return;
        }

        ItemStack stack = slot.getStack();
        if (!isActiveBonusSlot(stack)) {
            return;
        }

        Integer armorKey = currentArmorKey != null ? currentArmorKey : findCurrentArmorKey(minecraftClient);
        if (armorKey == null) {
            return;
        }

        int slotIndex = getScreenSlotIndex(minecraftClient, slot);
        if (slotIndex < 0 || !isArmorRollSlotLocked(armorKey, slotIndex)) {
            return;
        }

        int alpha = ((int) (0.55f * 255f) << 24);
        drawContext.getMatrices().push();
        try {
            drawContext.getMatrices().translate(0, 0, 290);
            drawContext.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, alpha | 0xFF3B30);
        } finally {
            drawContext.getMatrices().pop();
        }
    }

    private Slot getFocusedSlot(MinecraftClient minecraftClient) {
        if (!(minecraftClient.currentScreen instanceof HandledScreen<?> handledScreen)) {
            return null;
        }
        return ((HandledScreenAccessor) handledScreen).getFocusedSlot();
    }

    private Integer findCurrentArmorKey(MinecraftClient minecraftClient) {
        if (minecraftClient.player == null) {
            return null;
        }

        for (Slot slot : minecraftClient.player.currentScreenHandler.slots) {
            if (slot.inventory == minecraftClient.player.getInventory()) {
                continue;
            }

            Armor armor = Armor.getArmor(slot.getStack());
            if (armor != null) {
                Integer key = getArmorKey(slot.getStack());

                return key;
            }
        }

        return null;
    }

    private Integer getArmorKey(ItemStack itemStack) {
        NbtCompound data = ItemStackHelper.getNbt(itemStack);
        if (data == null) {
            return null;
        }

        NbtCompound customData = data.contains("custom_data", NbtElement.COMPOUND_TYPE)
                ? data.getCompound("custom_data")
                : data;

        String piece = customData.getString("piece");
        String name = customData.getString("name");

        if (!piece.isEmpty() || !name.isEmpty()) {
            StringBuilder signature = new StringBuilder();
            signature.append(piece).append("|").append(name).append("|");

            if (customData.contains("quality", NbtElement.NUMBER_TYPE)) {
                signature.append(customData.getInt("quality")).append("|");
            }
            if (customData.contains("uuid", NbtElement.INT_ARRAY_TYPE)) {
                int[] uuid = customData.getIntArray("uuid");
                if (uuid.length == 4) {
                    for (int i : uuid)
                        signature.append(i).append(",");
                }
                signature.append("|");
            }

            if (customData.contains("fish_bonus", NbtElement.LIST_TYPE)) {
                NbtList fishBonus = customData.getList("fish_bonus", NbtElement.COMPOUND_TYPE);
                List<String> bonusIds = new ArrayList<>();
                for (int i = 0; i < fishBonus.size(); i++) {
                    NbtCompound bonus = fishBonus.getCompound(i);
                    String id = bonus.getString("id");
                    if (!id.isEmpty()) {
                        bonusIds.add(id);
                    }
                }

                Collections.sort(bonusIds);
                for (String id : bonusIds) {
                    signature.append(id).append("|");
                }
            }

            return signature.toString().hashCode();
        }

        return null;
    }

    private int getScreenSlotIndex(MinecraftClient minecraftClient, Slot slot) {
        if (minecraftClient.player == null) {
            return -1;
        }
        return minecraftClient.player.currentScreenHandler.slots.indexOf(slot);
    }

    public boolean isLockedArmorRollSlot(MinecraftClient minecraftClient, Slot slot) {
        if (!armorRollsMenuState || minecraftClient == null || minecraftClient.player == null || slot == null) {
            return false;
        }

        if (slot.inventory == minecraftClient.player.getInventory()) {
            return false;
        }

        ItemStack stack = slot.getStack();
        if (!isActiveBonusSlot(stack)) {
            return false;
        }

        Integer armorKey = currentArmorKey != null ? currentArmorKey : findCurrentArmorKey(minecraftClient);
        if (armorKey == null) {
            return false;
        }

        int slotIndex = getScreenSlotIndex(minecraftClient, slot);
        if (slotIndex < 0) {
            return false;
        }

        if (isArmorRollSlotLocked(armorKey, slotIndex)) {
            return true;
        }

        return isArmorRollSlotLocked(armorKey, slotIndex);
    }

    private boolean isArmorRollSlotLocked(int armorKey, int slotIndex) {
        Map<Integer, List<String>> lockedArmorRolls = ProfileDataHandler.instance().profileData.lockedArmorRolls;
        if (lockedArmorRolls == null) {
            return false;
        }
        List<String> lockedSlots = lockedArmorRolls.get(armorKey);
        if (lockedSlots == null || lockedSlots.isEmpty()) {
            return false;
        }
        return lockedSlots.contains(String.valueOf(slotIndex));
    }


    private boolean isActiveBonusSlot(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        if (itemStack.getItem() != Items.ENDER_PEARL && itemStack.getItem() != Items.ENDER_EYE) {
            return false;
        }

        List<Text> loreLines = getLoreLines(itemStack);
        if (loreLines == null) {
            return false;
        }
        for (Text line : loreLines) {
            String text = line.getString().toLowerCase();
            if (text.contains("active bonus slot") || text.contains("empty bonus slot")) {
                return true;
            }
        }

        return false;
    }

    private List<Text> getLoreLines(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return null;
        }

        if (itemStack.get(DataComponentTypes.LORE) == null) {
            return null;
        }

        return Objects.requireNonNull(itemStack.get(DataComponentTypes.LORE)).lines();
    }

    private void updateLore(ItemStack stack, boolean locked) {
        LoreComponent loreComponent = stack.get(DataComponentTypes.LORE);
        if (loreComponent == null)
            return;

        List<Text> lines = new ArrayList<>(loreComponent.lines());
        boolean changed = false;
        int lockedColorValue = 0xA72020;

        for (int i = 0; i < lines.size(); i++) {
            Text line = lines.get(i);
            List<Text> siblings = line.getSiblings();

            if (!siblings.isEmpty()) {
                Text firstSibling = siblings.get(0);
                Style style = firstSibling.getStyle();
                TextColor color = style.getColor();

                boolean isLockedColor = color != null && color.getRgb() == lockedColorValue;
                boolean shouldUpdate = false;

                if (locked && !isLockedColor) {
                    shouldUpdate = true;
                } else if (!locked && isLockedColor) {
                    shouldUpdate = true;
                }

                if (shouldUpdate) {
                    Style newStyle = style.withColor(locked ? TextColor.fromRgb(lockedColorValue)
                            : TextColor.fromFormatting(Formatting.WHITE));

                    MutableText newFirstSibling = firstSibling.copy();
                    newFirstSibling.setStyle(newStyle);

                    MutableText newLine = Text.empty().setStyle(line.getStyle());
                    newLine.append(newFirstSibling);
                    for (int j = 1; j < siblings.size(); j++) {
                        newLine.append(siblings.get(j));
                    }
                    lines.set(i, newLine);
                    changed = true;
                }
            }
        }

        if (changed) {
            stack.set(DataComponentTypes.LORE, new LoreComponent(lines));
        }
    }
}
