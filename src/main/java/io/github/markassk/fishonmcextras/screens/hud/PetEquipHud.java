package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.Pet;
import io.github.markassk.fishonmcextras.common.FlairDecor;
import io.github.markassk.fishonmcextras.common.Theming;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.PetEquipHandler;
import io.github.markassk.fishonmcextras.handler.ProfileDataHandler;
import io.github.markassk.fishonmcextras.handler.ThemingHandler;
import io.github.markassk.fishonmcextras.handler.screens.hud.PetEquipHudHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PetEquipHud {
    public void render(DrawContext drawContext, MinecraftClient client) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        // Assemble all text lines
        List<Text> textList = PetEquipHudHandler.instance().assemblePetText();
        ItemStack activePet = PetEquipHandler.instance().currentPetItem;
        ItemStack activePetItem = PetEquipHandler.instance().currentPetItemItem;

        drawContext.getMatrices().push();
        try {
            // Get screen size
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            boolean rightAlignment = config.petEquipTracker.activePetHUDOptions.rightAlignment;

            // Convert percentage config values to screen coordinates
            float xPercent = rightAlignment ? 1f - (config.petEquipTracker.activePetHUDOptions.hudX / 100f)
                    : config.petEquipTracker.activePetHUDOptions.hudX / 100f;
            float yPercent = config.petEquipTracker.activePetHUDOptions.hudY / 100f;

            // Calculate base positions relative to screen size
            int baseX = (int) (screenWidth * xPercent);
            int baseY = (int) (screenHeight * yPercent);

            // Scaling setup
            int fontSize = config.petEquipTracker.activePetHUDOptions.fontSize;
            float scale = fontSize / 10.0f;
            drawContext.getMatrices().scale(scale, scale, 1f);

            // Alpha
            int alphaInt = (int) ((config.petEquipTracker.activePetHUDOptions.backgroundOpacity / 100f) * 255f) << 24;

            int lineSpacing = 2;
            int lineHeight = (int) (textRenderer.fontHeight + (lineSpacing / scale));
            int scaledX = (int) (baseX / scale);
            int scaledY = (int) (baseY / scale);
            int padding = 8;
            int itemBoxGap = 4;
            boolean itemBoxVisible = config.petEquipTracker.activePetHUDOptions.showItemIcon && activePetItem != null;
            int extraBoxWidth = itemBoxVisible ? (16 + padding + itemBoxGap) : 0;
            AtomicInteger count = new AtomicInteger(0);

            int maxLength = textList.stream().map(textRenderer::getWidth).max(Integer::compareTo).orElse(0);
            int heightClampTranslation = (int) ((padding * 2 + textList.size() * lineHeight) * yPercent);
            heightClampTranslation -= (int) ((padding * 3) * (1 - yPercent));

            // Draw Background
            Constant ratingTag = Constant.DEFAULT;
            if (config.petEquipTracker.activePetHUDOptions.colorPetBorderToRating
                    && ProfileDataHandler.instance().profileData != null
                    && ProfileDataHandler.instance().profileData.equippedPet != null) {
                ratingTag = Pet.getConstantFromPercent(
                        ProfileDataHandler.instance().profileData.equippedPet.percentPetRating);
            }
            int borderColor = config.petEquipTracker.activePetHUDOptions.colorPetBorderToRating
                    ? ratingTag.COLOR
                    : 0xFFFFFF;
            if (rightAlignment) {
                drawContext.fill(scaledX, scaledY - heightClampTranslation,
                        scaledX - maxLength - padding * 2 - 16 - padding - extraBoxWidth,
                        scaledY + ((textList.size() - 1) * lineHeight) + padding * 3 - heightClampTranslation,
                        alphaInt);
                drawContext.drawBorder(scaledX - padding + padding / 2,
                        scaledY + padding + 1 - padding / 2 - heightClampTranslation, -16 - padding, 16 + padding,
                        alphaInt | borderColor);
                if (itemBoxVisible) {
                    drawContext.drawBorder(scaledX - padding + padding / 2 - (16 + padding + itemBoxGap),
                            scaledY + padding + 1 - padding / 2 - heightClampTranslation, -16 - padding,
                            16 + padding, alphaInt | 0xFFFFFF);
                }
            } else {
                drawContext.fill(scaledX, scaledY - heightClampTranslation,
                        scaledX + maxLength + padding * 2 + 16 + padding + extraBoxWidth,
                        scaledY + ((textList.size() - 1) * lineHeight) + padding * 3 - heightClampTranslation,
                        alphaInt);
                drawContext.drawBorder(scaledX + padding - padding / 2,
                        scaledY + padding + 1 - padding / 2 - heightClampTranslation, 16 + padding, 16 + padding,
                        alphaInt | borderColor);
                if (itemBoxVisible) {
                    drawContext.drawBorder(scaledX + padding - padding / 2 + 16 + padding + itemBoxGap,
                            scaledY + padding + 1 - padding / 2 - heightClampTranslation, 16 + padding,
                            16 + padding, alphaInt | 0xFFFFFF);
                }
            }

            if (PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.HAS_PET) {
                // Draw Pet and Pet Item
                if (rightAlignment) {
                    drawContext.drawItem(activePet, scaledX - padding - 16,
                            scaledY + padding + 1 - heightClampTranslation);
                    if (itemBoxVisible) {
                                                drawContext.drawItem(activePetItem, scaledX - padding - 16 - (16 + padding + itemBoxGap),
                                scaledY + padding + 1 - heightClampTranslation);
                    }
                } else {
                    drawContext.drawItem(activePet, scaledX + padding, scaledY + padding + 1 - heightClampTranslation);
                    if (itemBoxVisible) {
                                                drawContext.drawItem(activePetItem, scaledX + padding + 16 + padding + itemBoxGap,
                                scaledY + padding + 1 - heightClampTranslation);
                    }
                }
            }

            // Theming
            FlairDecor flairDecor = ThemingHandler.instance().flairDecorPetEquip;
            int rightAlignmentOffset = (rightAlignment ? padding * 3 + maxLength + 16 + extraBoxWidth : 0);

            if (config.theme.themeType != Theming.ThemeType.OFF) {
                Theming theme = ThemingHandler.instance().currentTheme;
                int colorOverlay = config.theme.colorOverlay;
                int themeTextColor = ThemingHandler.instance().currentThemeType.TEXT_COLOR;
                int alphaOverlay = (int) ((config.theme.opacity / 100f) * 255f) << 24;

                // Corners
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP_LEFT,
                        scaledX - padding - rightAlignmentOffset, scaledY - padding - heightClampTranslation, 16, 16,
                        alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP_RIGHT,
                        scaledX + padding * 2 + maxLength + 16 + extraBoxWidth - rightAlignmentOffset,
                        scaledY - padding - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM_LEFT,
                        scaledX - padding - rightAlignmentOffset,
                        scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation, 16, 16,
                        alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM_RIGHT,
                        scaledX + padding * 2 + maxLength + 16 + extraBoxWidth - rightAlignmentOffset,
                        scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation, 16, 16,
                        alphaOverlay | colorOverlay);

                // Sides
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_LEFT,
                        scaledX - padding - rightAlignmentOffset, scaledY + padding - heightClampTranslation, 16,
                        ((textList.size() - 1) * lineHeight) + padding, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_RIGHT,
                        scaledX + padding * 2 + 16 + maxLength + extraBoxWidth - rightAlignmentOffset,
                        scaledY + padding - heightClampTranslation, 16, ((textList.size() - 1) * lineHeight) + padding,
                        alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP,
                        scaledX + padding - rightAlignmentOffset, scaledY - padding - heightClampTranslation,
                        maxLength + padding + 16 + extraBoxWidth, 16, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM,
                        scaledX + padding - rightAlignmentOffset,
                        scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation,
                        maxLength + padding + 16 + extraBoxWidth, 16, alphaOverlay | colorOverlay);

                // Title
                Text title = Text.literal("ᴘᴇᴛ").withColor(ThemingHandler.instance().currentThemeType.TEXT_COLOR)
                        .formatted(Formatting.BOLD);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TEXT_LEFT,
                        scaledX + (maxLength + padding * 3 + 16 + extraBoxWidth) / 2 - textRenderer.getWidth(title) / 2
                                - 16
                                - rightAlignmentOffset,
                        scaledY - padding - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TEXT_MIDDLE,
                        scaledX + (maxLength + padding * 3 + 16 + extraBoxWidth) / 2 - textRenderer.getWidth(title) / 2
                                - rightAlignmentOffset,
                        scaledY - padding - heightClampTranslation, textRenderer.getWidth(title), 16,
                        alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TEXT_RIGHT,
                        scaledX + (maxLength + padding * 3 + 16 + extraBoxWidth) / 2 + textRenderer.getWidth(title) / 2
                                - rightAlignmentOffset,
                        scaledY - padding - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                drawContext.drawText(textRenderer, title,
                        scaledX + (maxLength + padding * 3 + 16 + extraBoxWidth) / 2 - textRenderer.getWidth(title) / 2
                                - rightAlignmentOffset,
                        scaledY - textRenderer.fontHeight / 2 - heightClampTranslation - 1, themeTextColor, false);
            }

            // Flair
            drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_TOP_LEFT,
                    scaledX - padding - rightAlignmentOffset - 24, scaledY - padding - heightClampTranslation - 24, 64,
                    64);
            drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_TOP_RIGHT,
                    scaledX + padding * 2 + maxLength + 16 + extraBoxWidth - rightAlignmentOffset - 24,
                    scaledY - padding - heightClampTranslation - 24, 64, 64);
            drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_BOTTOM_LEFT,
                    scaledX - padding - rightAlignmentOffset - 24,
                    scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation - 24, 64, 64);
            drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_BOTTOM_RIGHT,
                    scaledX + padding * 2 + maxLength + 16 + extraBoxWidth - rightAlignmentOffset - 24,
                    scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation - 24, 64, 64);

            // Draw Text
            int finalHeightClampTranslation = heightClampTranslation;
            textList.forEach(text -> drawContext.drawText(textRenderer, text,
                    rightAlignment ? scaledX - padding - 16 - padding - extraBoxWidth - textRenderer.getWidth(text)
                            : scaledX + padding + 16 + padding + extraBoxWidth,
                    scaledY + (count.getAndIncrement() * lineHeight) + padding - finalHeightClampTranslation, 0xFFFFFF,
                    true));
        } finally {
            drawContext.getMatrices().pop();
        }
    }
}
