package io.github.markassk.fishonmcextras.handler.screens.hud;

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
        return value <= 25 ? Formatting.RED :
                value <= 50 ? Formatting.GOLD :
                        value <= 75 ? Formatting.YELLOW :
                                Formatting.GREEN;
    }

    public List<Text> assemblePetText() {
        ProfileDataHandler.ProfileData profileData = ProfileDataHandler.instance().profileData;

        List<Text> textList = new ArrayList<>();

        if (PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.HAS_PET) {
            Text namePet = PetEquipHandler.instance().currentPetItem.getName();
            int level = profileData.equippedPet.lvl;
            float currentXp = profileData.equippedPet.currentXp;
            float neededXp = profileData.equippedPet.neededXp;
            float percentXp = currentXp / neededXp * 100f;

            Formatting levelColor = getProgressColor(level);
            textList.add(TextHelper.concat(
                    profileData.equippedPet.rarity.TAG,
                    Text.literal(" "),
                    namePet,
                    Text.literal(" (").formatted(Formatting.DARK_GRAY),
                    Text.literal("ʟᴠʟ ").formatted(Formatting.GRAY),
                    Text.literal(String.valueOf(level)).formatted(levelColor),
                    Text.literal(")").formatted(Formatting.DARK_GRAY)
            ));
            if(level == 100) {
                textList.add(TextHelper.concat(
                        Text.literal("(").formatted(Formatting.DARK_GRAY),
                        Text.literal(TextHelper.fmnt(currentXp)).formatted(Formatting.AQUA),
                        Text.literal("/").formatted(Formatting.DARK_GRAY),
                        Text.literal("MAX").formatted(Formatting.BLUE),
                        Text.literal(") ").formatted(Formatting.DARK_GRAY),
                        Text.literal("100").formatted(Formatting.GREEN),
                        Text.literal("%").formatted(Formatting.GREEN)
                ));
            } else {
                Formatting percentColor = getProgressColor(percentXp);
                textList.add(TextHelper.concat(
                        Text.literal("(").formatted(Formatting.DARK_GRAY),
                        Text.literal(TextHelper.fmnt(currentXp)).formatted(Formatting.AQUA),
                        Text.literal("/").formatted(Formatting.DARK_GRAY),
                        Text.literal(TextHelper.fmnt(neededXp)).formatted(Formatting.BLUE),
                        Text.literal(") ").formatted(Formatting.DARK_GRAY),
                        Text.literal(TextHelper.fmt(percentXp, 1)).formatted(percentColor),
                        Text.literal("%").formatted(percentColor)
                ));
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
}
