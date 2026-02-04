package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.Pet;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.PetEquipHandler;
import io.github.markassk.fishonmcextras.handler.ProfileDataHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class PetEquipHudHandler {
    private static PetEquipHudHandler INSTANCE = new PetEquipHudHandler();

    public static PetEquipHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new PetEquipHudHandler();
        }
        return INSTANCE;
    }

    private Formatting getProgressColor(double value) {
        return value <= 25 ? Formatting.RED
                : value <= 50 ? Formatting.GOLD : value <= 75 ? Formatting.YELLOW : Formatting.GREEN;
    }

    public List<Text> assemblePetText() {
        ProfileDataHandler.ProfileData profileData = ProfileDataHandler.instance().profileData;
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

        List<Text> textList = new ArrayList<>();

        if (PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.HAS_PET) {
            Text namePet = PetEquipHandler.instance().currentPetItem.getName();
            int level = profileData.equippedPet.lvl;
            float currentXp = profileData.equippedPet.currentXp;
            float neededXp = profileData.equippedPet.neededXp;
            float percentXp = currentXp / neededXp * 100f;
            float petPercent = profileData.equippedPet.percentPetRating * 100f;
            Constant ratingTag = Pet.getConstantFromPercent(profileData.equippedPet.percentPetRating);
            String petItemId = profileData.equippedPet.petItem;
            Constant petItemConstant = petItemId != null ? Constant.valueOfId(petItemId) : null;
            Text petItemText = null;
            if (petItemId != null) {
                if (petItemConstant != null
                        && petItemConstant != Constant.DEFAULT
                        || Constant.DEFAULT.ID.equals(petItemId)) {
                    petItemText = petItemConstant != null ? petItemConstant.TAG : null;
                } else {
                    petItemText = Text.literal(petItemId);
                }
            }

            Formatting levelColor = getProgressColor(level);
            textList.add(TextHelper.concat(
                    profileData.equippedPet.rarity.TAG,
                    Text.literal(" "),
                    namePet,
                    Text.literal(" (").formatted(Formatting.DARK_GRAY),
                    Text.literal("ʟᴠʟ ").formatted(Formatting.GRAY),
                    Text.literal(String.valueOf(level)).formatted(levelColor),
                    Text.literal(")").formatted(Formatting.DARK_GRAY)));

            if (level == 100) {
                if (config.petEquipTracker.activePetHUDOptions.xpDisplayType == XpDisplayType.ALL) {
                    textList.add(TextHelper.concat(
                            Text.literal("xᴘ ").formatted(Formatting.GRAY),
                            Text.literal("(").formatted(Formatting.DARK_GRAY),
                            Text.literal(TextHelper.fmnt(currentXp)).formatted(Formatting.AQUA),
                            Text.literal("/").formatted(Formatting.DARK_GRAY),
                            Text.literal("MAX").formatted(Formatting.BLUE),
                            Text.literal(") ").formatted(Formatting.DARK_GRAY),
                            Text.literal("100").formatted(Formatting.GREEN),
                            Text.literal("%").formatted(Formatting.GREEN)));
                } else if (config.petEquipTracker.activePetHUDOptions.xpDisplayType == XpDisplayType.SHORT) {
                    textList.add(TextHelper.concat(
                            Text.literal("xᴘ ").formatted(Formatting.GRAY),
                            Text.literal("(").formatted(Formatting.DARK_GRAY),
                            Text.literal(TextHelper.fmnt(currentXp)).formatted(Formatting.AQUA),
                            Text.literal(")").formatted(Formatting.DARK_GRAY)));
                }
            } else {
                Formatting percentColor = getProgressColor(percentXp);
                if (config.petEquipTracker.activePetHUDOptions.xpDisplayType == XpDisplayType.ALL) {
                    textList.add(TextHelper.concat(
                            Text.literal("xᴘ ").formatted(Formatting.GRAY),
                            Text.literal("(").formatted(Formatting.DARK_GRAY),
                            Text.literal(TextHelper.fmnt(currentXp)).formatted(Formatting.AQUA),
                            Text.literal("/").formatted(Formatting.DARK_GRAY),
                            Text.literal(TextHelper.fmnt(neededXp)).formatted(Formatting.BLUE),
                            Text.literal(") ").formatted(Formatting.DARK_GRAY),
                            Text.literal(TextHelper.fmt(percentXp, 1)).formatted(percentColor),
                            Text.literal("%").formatted(percentColor)));
                } else if (config.petEquipTracker.activePetHUDOptions.xpDisplayType == XpDisplayType.SHORT) {
                    textList.add(TextHelper.concat(
                            Text.literal("xᴘ ").formatted(Formatting.GRAY),
                            Text.literal("(").formatted(Formatting.DARK_GRAY),
                            Text.literal(TextHelper.fmnt(currentXp)).formatted(Formatting.AQUA),
                            Text.literal(") ").formatted(Formatting.DARK_GRAY),
                            Text.literal(TextHelper.fmt(percentXp, 1)).formatted(percentColor),
                            Text.literal("%").formatted(Formatting.DARK_GRAY)));
                }
            }
            if (config.petEquipTracker.activePetHUDOptions.ratingDisplayType == RatingDisplayType.ALL) {
                textList.add(TextHelper.concat(
                        Text.literal("ʀᴀᴛɪɴɢ ").formatted(Formatting.GRAY),
                        ratingTag.TAG,
                        Text.literal(" "),
                        Text.literal("(").formatted(Formatting.DARK_GRAY),
                        Text.literal(TextHelper.fmt(petPercent, 1) + "%").withColor(ratingTag.COLOR),
                        Text.literal(")").formatted(Formatting.DARK_GRAY)));
            } else if (config.petEquipTracker.activePetHUDOptions.ratingDisplayType == RatingDisplayType.SHORT) {
                textList.add(TextHelper.concat(
                        Text.literal("ʀᴀᴛɪɴɢ ").formatted(Formatting.GRAY),
                        Text.literal("(").formatted(Formatting.DARK_GRAY),
                        Text.literal(TextHelper.fmt(petPercent, 1) + "%").withColor(ratingTag.COLOR),
                        Text.literal(")").formatted(Formatting.DARK_GRAY)));
            }
            if (petItemText != null) {
                if (config.petEquipTracker.activePetHUDOptions.itemDisplayType == ItemDisplayType.ALL) {
                    textList.add(TextHelper.concat(
                            Text.literal("ɪᴛᴇᴍ ").formatted(Formatting.GRAY),
                            petItemText));
                }
            }
        } else if (PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.NO_PET) {
            textList.add(Text.literal("No pet equipped").formatted(Formatting.RED));
            textList.add(Text.empty());
        } else if (PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.LOADING) {
            textList.add(Text.literal("Loading").formatted(Formatting.RED));
            textList.add(Text.empty());
        }

        return textList;
    }

    public enum XpDisplayType {
        ALL,
        SHORT
    }

    public enum RatingDisplayType {
        NONE,
        ALL,
        SHORT
    }

    public enum ItemDisplayType {
        NONE,
        ALL
    }
}
