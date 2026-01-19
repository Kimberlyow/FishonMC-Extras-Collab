package io.github.markassk.fishonmcextras.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.markassk.fishonmcextras.FOMC.Constant;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class TextHelper {
    private static final Gson gson = new Gson();

    public static MutableText concat(Text... texts) {
        MutableText text = Text.empty();
        for (Text t : texts) {
            text.append(t);
        }
        return text;
    }

    // Format to string
    public static String fmt(float d)
    {
        return String.format(Locale.US,"%.0f", d);
    }

    public static String fmt(float d, int decimalPlaces) {
        switch (decimalPlaces) {
            case 1 -> {
                String s = String.format(Locale.US,"%.1f", d);
                return s.replaceAll("0*$", "").replaceAll("[.,]$", "");
            }
                case 2 -> {
                String s = String.format(Locale.US,"%.2f", d);
                return s.replaceAll("0*$", "").replaceAll("[.,]$", "");
            }
            default -> {
                String s = String.format(Locale.US,"%.0f", d);
                return s.replaceAll("0*$", "").replaceAll("[.,]$", "");
            }
        }
    }

    // Format to number string
    public static String fmnt(float d) {
        if (d >= 1000 && d < 1000000) {
            String s = String.format(Locale.US, "%.2f", d / 1000);
            return s.replaceAll("0*$", "").replaceAll("[.,]$", "") + "K";
        } else if (d > 1000000 && d < 1000000000) {
            String s = String.format(Locale.US, "%.2f", d / 1000000);
            return s.replaceAll("0*$", "").replaceAll("[.,]$", "") + "M";
        } else if (d > 1000000000) {
            String s = String.format(Locale.US, "%.2f", d / 1000000000);
            return s.replaceAll("0*$", "").replaceAll("[.,]$", "") + "B";
        } else if (d == 0) {
            return "0";
        } else {
            return String.format(Locale.US, "%.0f", d);
        }
    }

    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

    public static String capitalize(String str) {
        if(str == null || str.length()<=1) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String textToJson(Text text) {
        return gson.toJson(TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, text).getOrThrow());
    }

    public static Text jsonToText(String text) {
        return TextCodecs.CODEC
                .decode(JsonOps.INSTANCE, gson.fromJson(text, JsonElement.class))
                .getOrThrow()
                .getFirst();
    }

    public static String upperCaseAllFirstCharacter(String text) {
        String regex = "\\b(.)(.*?)\\b";
        return Pattern.compile(regex).matcher(text).replaceAll(
                matched -> matched.group(1).toUpperCase() + matched.group(2)
        );
    }

    public static float roundFirstSignificantDigit(float input) {
        if (!Float.isNaN(input) && !Float.isInfinite(input)) {
            if(input >= 0.1f || input == 0) {
                return input;
            }

            int precision = 0;
            float val = input - Math.round(input);
            while (Math.abs(val) < 1) {
                val *= 10;
                precision++;
            }
            return BigDecimal.valueOf(input).setScale(precision, RoundingMode.HALF_UP).floatValue();
        }
        return input;
    }

    public static String replaceToFoE(String text) {
        if(text.contains(Constant.ANGLER.TAG.getString())) text = text.replace(Constant.ANGLER.TAG.getString(), Constant.FOE.TAG.getString());
        if(text.contains(Constant.SAILOR.TAG.getString())) text = text.replace(Constant.SAILOR.TAG.getString(), Constant.FOE.TAG.getString());
        if(text.contains(Constant.MARINER.TAG.getString())) text = text.replace(Constant.MARINER.TAG.getString(), Constant.FOE.TAG.getString());
        if(text.contains(Constant.CAPTAIN.TAG.getString())) text = text.replace(Constant.CAPTAIN.TAG.getString(), Constant.FOE.TAG.getString());
        if(text.contains(Constant.ADMIRAL.TAG.getString())) text = text.replace(Constant.ADMIRAL.TAG.getString(), Constant.FOE.TAG.getString());
        return text;
    }

    /**
     * Splits a Text object by newlines and returns a list of Text objects, one per line.
     * This preserves the original Text structure and formatting by traversing the Text tree.
     */
    public static List<Text> splitByNewlines(Text text) {
        List<Text> lines = new ArrayList<>();
        if (text == null) {
            return lines;
        }

        // Check if there are any newlines first
        if (!text.getString().contains("\n")) {
            lines.add(text);
            return lines;
        }

        // Build lines by processing the root and siblings sequentially
        MutableText currentLine = Text.empty();

        // Process root content if it's not empty (Text.empty() creates an empty root)
        String rootContent = getRootContent(text);
        if (!rootContent.isEmpty() && !rootContent.equals("\n")) {
            if (rootContent.contains("\n")) {
                // Split root content by newlines
                String[] parts = rootContent.split("\n", -1);
                for (int i = 0; i < parts.length; i++) {
                    if (!parts[i].isEmpty()) {
                        MutableText partText = Text.literal(parts[i]);
                        if (text.getStyle() != null) {
                            partText.setStyle(text.getStyle());
                        }
                        currentLine.append(partText);
                    }
                    if (i < parts.length - 1) {
                        if (!currentLine.getString().isEmpty()) {
                            lines.add(currentLine);
                        }
                        currentLine = Text.empty();
                    }
                }
            } else {
                // Add root content to current line
                MutableText rootText = Text.literal(rootContent);
                if (text.getStyle() != null) {
                    rootText.setStyle(text.getStyle());
                }
                currentLine.append(rootText);
            }
        }

        // Process siblings sequentially
        for (Text sibling : text.getSiblings()) {
            String siblingContent = sibling.getString();

            // Check if this sibling is a newline
            if (siblingContent.equals("\n")) {
                // Save current line and start a new one
                if (!currentLine.getString().isEmpty()) {
                    lines.add(currentLine);
                }
                currentLine = Text.empty();
                continue;
            }

            // Check if sibling content contains newlines
            if (siblingContent.contains("\n")) {
                // Split the sibling content by newlines
                String[] parts = siblingContent.split("\n", -1);
                for (int i = 0; i < parts.length; i++) {
                    if (!parts[i].isEmpty()) {
                        // Create a new Text with the same style as the sibling
                        MutableText partText = Text.literal(parts[i]);
                        if (sibling.getStyle() != null) {
                            partText.setStyle(sibling.getStyle());
                        }
                        currentLine.append(partText);
                    }

                    // After each part except the last, start a new line
                    if (i < parts.length - 1) {
                        if (!currentLine.getString().isEmpty()) {
                            lines.add(currentLine);
                        }
                        currentLine = Text.empty();
                    }
                }
            } else if (!siblingContent.isEmpty()) {
                // No newlines, just append the sibling preserving its style
                currentLine.append(sibling.copy());
            }
        }

        // Add the last line if it's not empty
        if (!currentLine.getString().isEmpty()) {
            lines.add(currentLine);
        }

        return lines;
    }

    /**
     * Gets the root content string of a Text node without including its siblings.
     */
    private static String getRootContent(Text text) {
        // If the text has no siblings, getString() gives us just the content
        if (text.getSiblings().isEmpty()) {
            return text.getString();
        }

        // If it has siblings, we need to get just the root content
        // We can do this by getting the string and subtracting sibling strings
        String fullString = text.getString();
        StringBuilder siblingStrings = new StringBuilder();
        for (Text sibling : text.getSiblings()) {
            siblingStrings.append(sibling.getString());
        }

        // Remove sibling strings from the full string to get root content
        String rootContent = fullString;
        if (siblingStrings.length() > 0) {
            int siblingStart = fullString.indexOf(siblingStrings.toString());
            if (siblingStart >= 0) {
                rootContent = fullString.substring(0, siblingStart);
            }
        }

        return rootContent;
    }
}
