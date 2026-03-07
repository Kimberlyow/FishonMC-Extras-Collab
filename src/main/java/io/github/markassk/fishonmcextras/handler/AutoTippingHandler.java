package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.text.Style;

public class AutoTippingHandler {
	private static AutoTippingHandler INSTANCE;
	private long timestamp = 0L;

	public static AutoTippingHandler instance() {
		if (INSTANCE == null) {
			INSTANCE = new AutoTippingHandler();
		}
		return INSTANCE;
	}

	public boolean onReceiveMessage(Text message) {
		if (!LoadingHandler.instance().isOnServer) {
			return false;
		}

		String plain = message.getString();

		Pattern p = Pattern.compile("REACTIONS »\\s*([0-9A-Za-z_]{3,16})\\b");
		Matcher m = p.matcher(plain);

		if (m.find()) {
			String username = m.group(1);
			onReceiveReactions(username, message);
		}

		return false;
	}

	private void onReceiveReactions(String username, Text message) {
		MinecraftClient client = MinecraftClient.getInstance();
		client.execute(() -> {
			if (client.player == null) {
				return;
			}

			if (client.inGameHud == null) {
				return;
			}

			PlayerListEntry playerListEntry = client.getNetworkHandler() != null
					? client.getNetworkHandler().getPlayerListEntry(username)
					: null;

			if (playerListEntry == null) {
				return;
			}

			if (client.player.getName().getString().equals(username)) {
				return;
			}

			if (timestamp + 5000L > System.currentTimeMillis()) {
				return;
			}
			timestamp = System.currentTimeMillis();

			int amount = FishOnMCExtrasConfig.getConfig().autoTip.reactionTipAmount;
			boolean autoTip = FishOnMCExtrasConfig.getConfig().autoTip.autoTipReactions;
			String payCommand = "/pay " + username + " " + amount;

			if (!autoTip && !FishOnMCExtrasConfig.getConfig().autoTip.disableManualTippingMsg) {
				client.inGameHud.getChatHud().addMessage(
						Text.empty()
								.append(Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD))
								.append(Text.literal("| ").formatted(Formatting.DARK_GRAY))
								.append(Text.literal("Want to tip ").formatted(Formatting.GRAY))
								.append(Text.literal(username).formatted(Formatting.WHITE))
								.append(Text.literal(" $").formatted(Formatting.DARK_GREEN))
								.append(Text.literal(String.valueOf(amount)).formatted(Formatting.GREEN))
								.append(Text.literal("? ").formatted(Formatting.GRAY))
								.append(Text.literal("[").formatted(Formatting.DARK_GRAY))
								.append(Text.literal("Click").formatted(Formatting.YELLOW))
								.append(Text.literal("]").formatted(Formatting.DARK_GRAY))
								.setStyle(Style.EMPTY
										.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, payCommand))
										.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(payCommand)))));
				return;
			}

            if (!autoTip) {
                return;
            }

			if (client.player.networkHandler != null) {
				client.inGameHud.getChatHud().addMessage(
						Text.empty()
								.append(Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD))
								.append(Text.literal("| ").formatted(Formatting.DARK_GRAY))
								.append(Text.literal("You Auto tipped ").formatted(Formatting.GRAY))
								.append(Text.literal(username).formatted(Formatting.WHITE))
								.append(Text.literal(" $").formatted(Formatting.DARK_GREEN))
								.append(Text.literal(String.valueOf(amount)).formatted(Formatting.GREEN))
								.append(Text.literal("!").formatted(Formatting.GRAY)));
				client.player.networkHandler.sendChatCommand("pay " + username + " " + amount);
			}
		});
	}
}
