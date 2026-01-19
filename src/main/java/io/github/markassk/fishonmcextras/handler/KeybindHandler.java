package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.mixin.KeyBindingAccessor;
import io.github.markassk.fishonmcextras.screens.main.MainScreen;
import io.github.markassk.fishonmcextras.util.AdvancedKeyBinding;
import io.github.markassk.fishonmcextras.util.TextHelper;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
    private static KeybindHandler INSTANCE = new KeybindHandler();

    public final AdvancedKeyBinding openConfigKeybind = new AdvancedKeyBinding("key.fishonmcextras.openconfig",
            GLFW.GLFW_KEY_O, "category.fishonmcextras.general");
    public final AdvancedKeyBinding openExtraInfoKeybind = new AdvancedKeyBinding("key.fishonmcextras.openextrainfo",
            GLFW.GLFW_KEY_Z, "category.fishonmcextras.general");
    public final AdvancedKeyBinding baitSortingHelper = new AdvancedKeyBinding("key.fishonmcextras.baitsortinghelper",
            GLFW.GLFW_KEY_B, "category.fishonmcextras.general");

    public boolean showExtraInfo = false;
    public boolean visualizeBaitSorting = false;

    public static KeybindHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new KeybindHandler();
        }
        return INSTANCE;
    }

    public void init() {
        KeybindHandler.register(
                this.openConfigKeybind,
                this.openExtraInfoKeybind,
                this.baitSortingHelper);
    }

    public void tick(MinecraftClient minecraftClient) {
        this.openConfigKeybind.onPressed(
                () -> minecraftClient.setScreen(new MainScreen(minecraftClient, minecraftClient.currentScreen)));

        this.baitSortingHelper.onPressed(() -> {
            boolean showOnlyWhilePressingKeybind = FishOnMCExtrasConfig
                    .getConfig().baitSortingHelperVisibility.showOnlyWhilePressingKeybind;

            if (!showOnlyWhilePressingKeybind) {
                boolean val = !BaitSortingHelperHandler.instance().toggle;
                BaitSortingHelperHandler.instance().toggle = val;
                if (minecraftClient.inGameHud != null) {
                    minecraftClient.inGameHud.getChatHud().addMessage(TextHelper.concat(
                            Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                            Text.literal("| ").formatted(Formatting.DARK_GRAY),
                            Text.literal("Sorting Helper "),
                            Text.literal(val ? "Enabled" : "Disabled")
                                    .formatted(val ? Formatting.GREEN : Formatting.RED)));
                }

                minecraftClient.getSoundManager().play(
                        PositionedSoundInstance.master(
                                SoundEvents.BLOCK_NOTE_BLOCK_PLING,
                                val ? 1.25f : 0.75f));
            }
        });

        if (minecraftClient.currentScreen != null) {
            this.showExtraInfo = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),
                    ((KeyBindingAccessor) openExtraInfoKeybind).getBoundKey().getCode());

            boolean showOnlyWhilePressingKeybind = FishOnMCExtrasConfig
                    .getConfig().baitSortingHelperVisibility.showOnlyWhilePressingKeybind;
            boolean isPressed = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),
                    ((KeyBindingAccessor) baitSortingHelper).getBoundKey().getCode());

            this.visualizeBaitSorting = showOnlyWhilePressingKeybind
                    ? isPressed
                    : BaitSortingHelperHandler.instance().toggle;
        }
    }

    private static void register(KeyBinding... keyBindings) {
        for (KeyBinding keyBinding : keyBindings) {
            KeyBindingHelper.registerKeyBinding(keyBinding);
        }
    }
}
