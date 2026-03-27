package io.github.markassk.fishonmcextras.mixin;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.Defaults;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.LoadingHandler;
import io.github.markassk.fishonmcextras.handler.ProfileDataHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;
import java.util.UUID;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @Unique
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;getPlayerName(Lnet/minecraft/client/network/PlayerListEntry;)Lnet/minecraft/text/Text;"))
    private Text injectRender(PlayerListHud instance, PlayerListEntry entry) {
        String playerUuid = entry.getProfile().getId().toString();
        Defaults.FoEDevType devType = Defaults.foeDevs.get(playerUuid);

        MutableText text;
        if(LoadingHandler.instance().isOnServer && devType != null) {
            Text originalName = instance.getPlayerName(entry);
            String jsonText = TextHelper.textToJson(originalName);
            jsonText = TextHelper.replaceToFoE(jsonText, devType.usePurpleTag);
            if (!devType.usePurpleTag) {
                jsonText = jsonText.replace("B05BF9", "00AF0E");
            }
            text = (MutableText) TextHelper.jsonToText(jsonText);
        } else {
            text = instance.getPlayerName(entry).copy();
        }

        if (config.friendTracker.showFriendTag && LoadingHandler.instance().isOnServer && ProfileDataHandler.instance().profileData.friends.contains(entry.getProfile().getId())) {
            text = config.friendTracker.isPrefix ? Text.literal("\uE00C ").append(text) : text.append(Text.literal(" \uE00C").formatted(Formatting.WHITE));
        } 

        if(config.crewTracker.showCrewTag && LoadingHandler.instance().isOnServer && ProfileDataHandler.instance().profileData.crewMembers.contains(entry.getProfile().getId())) {
            return config.crewTracker.isPrefix ? Text.literal("\uE00A ").append(text) : text.append(Text.literal(" \uE00A").formatted(Formatting.WHITE));
        } else {
            return text;
        }
    }
}
