package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Types.Defaults;
import io.github.markassk.fishonmcextras.handler.packet.PacketHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatScreenHandler {
    private static ChatScreenHandler INSTANCE = new ChatScreenHandler();

    public boolean screenInit = false;

    public static ChatScreenHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatScreenHandler();
        }
        return INSTANCE;
    }

    public Text appendTooltip(Text text) {
        String textString = text.getString();
        if (textString.startsWith("!")
                && textString.contains("»")) {
            Defaults.FoEDevType senderDev = Defaults.foeDevs.values().stream()
                    .filter(foEDevType -> textString.contains(foEDevType.text))
                    .findFirst()
                    .orElse(null);
            
            if (senderDev != null) {
                String jsonText = TextHelper.textToJson(text);
                jsonText = TextHelper.replaceToFoE(jsonText, senderDev.usePurpleTag);
                if (!senderDev.usePurpleTag) {
                    jsonText = jsonText.replace("B05BF9", "00AF0E");
                }
                return TextHelper.jsonToText(jsonText);
            }
        }
        return text;
    }

    public void onOpenScreen() {
        if (MinecraftClient.getInstance().player != null) {
            PacketHandler.TYPING_PACKET.sendStartTypingPacket(MinecraftClient.getInstance().player.getUuid());
        }
    }

    public void onRemoveScreen() {
        if (MinecraftClient.getInstance().player != null) {
            PacketHandler.TYPING_PACKET.sendStopTypingPacket(MinecraftClient.getInstance().player.getUuid());
        }
    }
}
