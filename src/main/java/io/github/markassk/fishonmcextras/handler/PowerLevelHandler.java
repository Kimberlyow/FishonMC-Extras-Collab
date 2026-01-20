package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowerLevelHandler {
    private static PowerLevelHandler INSTANCE = new PowerLevelHandler();
    private static final Pattern POWER_LEVEL_PATTERN = Pattern.compile("ᴘᴏᴡᴇʀ:\\s*([0-9.,]+[KM]?)", Pattern.CASE_INSENSITIVE);

    public static PowerLevelHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new PowerLevelHandler();
        }
        return INSTANCE;
    }

    public boolean onReceiveMessage(Text text) {
        // Only check messages, don't suppress
        return false;
    }

    public Text modifyMessage(Text text) {
        if (!LoadingHandler.instance().isOnServer) {
            return text;
        }

        // Extract all hover texts to find power level information
        List<Text> hoverTexts = TextHelper.extractAllHoverTexts(text);
        
        for (Text hoverText : hoverTexts) {
            String hoverTextString = hoverText.getString();
            
            // Check if this hover text contains power level information
            if (hoverTextString.contains("ᴘᴏᴡᴇʀ:")) {
                Matcher matcher = POWER_LEVEL_PATTERN.matcher(hoverTextString);
                if (matcher.find()) {
                    String powerLevel = matcher.group(1);
                    
                    // Append power level to the message
                    MutableText mutableText = text.copy();
                    mutableText.append(Text.literal(" ").formatted(Formatting.GRAY));
                    mutableText.append(Text.literal("[").formatted(Formatting.DARK_GRAY));
                    mutableText.append(Text.literal("ᴘᴏᴡᴇʀ: ").formatted(Formatting.GRAY));
                    mutableText.append(Text.literal(powerLevel).formatted(Formatting.WHITE));
                    mutableText.append(Text.literal("]").formatted(Formatting.DARK_GRAY));
                    
                    return mutableText;
                }
            }
        }

        return text;
    }
}
