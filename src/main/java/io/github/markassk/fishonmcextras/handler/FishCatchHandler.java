package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.Fish;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.packet.PacketHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.Objects;

public class FishCatchHandler  {
    private static FishCatchHandler INSTANCE = new FishCatchHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    private Text title = Text.empty();
    private Text subtitle = Text.empty();
    private final List<UUID> trackFishList = new ArrayList<>();
    private boolean fishFound = false;
    private boolean preCheck = false;
    private boolean isFull = false;
    private long fishCaughtTime = 0L;
    private boolean hasUsedRod = false;

    public long lastTimeUsedRod = 0L;

    public static FishCatchHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new FishCatchHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(minecraftClient.player == null || !LoadingHandler.instance().isLoadingDone || minecraftClient.world == null) {
            return;
        }

        if(minecraftClient.player.fishHook != null && !hasUsedRod) {
            hasUsedRod = true;
        } else if (hasUsedRod && minecraftClient.player.fishHook == null) {
            hasUsedRod = false;
            this.lastTimeUsedRod = System.currentTimeMillis();
        }

        if(this.preCheck) {
            this.updateTrackedFish(minecraftClient.player);
            this.preCheck = false;
            FishOnMCExtras.LOGGER.info("[FoE] Tracked Fish: {}", this.trackFishList.size());
        }

        if(this.fishFound) {
            if (System.currentTimeMillis() - this.fishCaughtTime < 2000L) {
                if (!this.isFull) {
                    int checkedStacks = 0;
                    for (int i = minecraftClient.player.getInventory().main.size() - 1; i >= 0; i--) {
                        ItemStack stack = minecraftClient.player.getInventory().main.get(i);
                        if(stack.isEmpty()) {
                            continue;
                        }
                        checkedStacks++;
                        this.processStack(stack, minecraftClient);
                        
                    }
                    if (checkedStacks > 0) {
                        FishOnMCExtras.LOGGER.debug("[FoE] Checked {} stacks for fish catch", checkedStacks);
                    }
                }

                if(FullInventoryHandler.instance().slotsLeft == 0) {
                    this.isFull = true;
                }
                
                

            } else {
                FishOnMCExtras.LOGGER.warn("[FoE] Fish not found after 2s - title: '{}', subtitle: '{}', isFull: {}", 
                    this.title.getString(), this.subtitle.getString(), this.isFull);
                this.fishFound = false;
                this.isFull = false;
                this.updateTrackedFish(minecraftClient.player);   
            }
        }

        ProfileDataHandler.instance().tickTimer();
    }

    public void tickEntities(Entity entity, MinecraftClient minecraftClient) {
        if(this.fishFound && this.isFull) {
            if(entity instanceof ItemEntity itemEntity) {
                ItemStack stack =  itemEntity.getStack();
                if(stack.isEmpty()) {
                    return;
                }
                this.processStack(stack, minecraftClient);
            }
        }
    }

    public void onJoinServer() {
        this.preCheck = true;
    }

    public void onLeaveServer() {
        this.fishFound = false;
    }

    public void catchTitle(Text title) {
        if(title.getString().length() != 1 || title.equals(Text.empty())) {
            return;
        }

        if(isFish(title.getString().charAt(0))) {
            this.title = title;
            this.fishFound = true;
            this.fishCaughtTime = System.currentTimeMillis();
        }
    }

    public void catchSubtitle(Text title) {
        if(title.getString().contains(Constant.COMMON.TAG.getString())
                || title.getString().contains(Constant.RARE.TAG.getString())
                || title.getString().contains(Constant.EPIC.TAG.getString())
                || title.getString().contains(Constant.LEGENDARY.TAG.getString())
                || title.getString().contains(Constant.MYTHICAL.TAG.getString())
                || title.getString().contains(Constant.SPECIAL.TAG.getString())
        ) {
            this.subtitle = title;
        }
    }

    public void reset() {
        LoadingHandler.instance().isLoadingDone = false;
        if(MinecraftClient.getInstance().player != null) {
            this.updateTrackedFish(MinecraftClient.getInstance().player);
        }
    }

    public boolean onReceiveMessage(Text text) {
        if(text.getString().startsWith("PET DROP! You pulled")) {
            int oldPetDryStreak = ProfileDataHandler.instance().profileData.petDryStreak;

            ProfileDataHandler.instance().updatePetCaughtStatsOnCatch();
            FishOnMCExtras.LOGGER.info("[FoE] Tracking Pet");

            if (config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.otherMessageToggles.showPet) {
                sendItemDryStreakMessage("pet", oldPetDryStreak);
            }
        }

        if(text.getString().startsWith("RARE CATCH! You pulled") && text.getString().contains("Shard")) {
            int oldShardDryStreak = ProfileDataHandler.instance().profileData.shardDryStreak;

            ProfileDataHandler.instance().updateShardCaughtStatsOnCatch(1);
            FishOnMCExtras.LOGGER.info("[FoE] Tracking Shard");

            if (config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.otherMessageToggles.showShard) {
                sendItemDryStreakMessage("shard", oldShardDryStreak);
            }
        }

        if(text.getString().startsWith("RARE CATCH! You pulled") && text.getString().contains("Lightning in a Bottle")) {
            int oldLightningBottleDryStreak = ProfileDataHandler.instance().profileData.lightningBottleDryStreak;

            ProfileDataHandler.instance().updateLightningBottleCaughtStatsOnCatch();
            FishOnMCExtras.LOGGER.info("[FoE] Tracking Lightning Bottle");

            if (config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.otherMessageToggles.showLightningBottle) {
                sendItemDryStreakMessage("lightning bottle", oldLightningBottleDryStreak);
            }
        }

        if(text.getString().startsWith("RARE CATCH! You pulled") && text.getString().contains("Infusion Capsule")) {
            ProfileDataHandler.instance().updateInfusionCapsuleCaughtStatsOnCatch();
            FishOnMCExtras.LOGGER.info("[FoE] Tracking Infusion Capsule");
        }
        
        return false; // Don't suppress any messages
    }

    private boolean isFish(char character) {
        return (int) character > 0xE000 && (int) character < 0xE999;
    }

    private void processStack(ItemStack stack, MinecraftClient minecraftClient) {
        Fish fish = Fish.getFish(stack);
        if (fish != null && this.fishFound) {
            String stackName = stack.getName().getString();
            String subtitle = this.subtitle.getString();
            boolean catcherMatches = minecraftClient.player != null && Objects.equals(fish.catcher, minecraftClient.player.getUuid());
            boolean notTracked = !trackFishList.contains(fish.id);
            boolean subtitleMatches = subtitle.contains(stackName);
            
            FishOnMCExtras.LOGGER.info("[FoE] Found fish: {} (variant: {}) - catcher: {}, notTracked: {}, subtitleMatch: {} (subtitle: '{}' contains name: '{}')", 
                stackName, fish.variant.ID, catcherMatches, notTracked, subtitleMatches, subtitle, stackName);
        }
        if (fish != null
                && minecraftClient.player != null
                && Objects.equals(fish.catcher, minecraftClient.player.getUuid())
                && !trackFishList.contains(fish.id)
                && this.subtitle.getString().contains(stack.getName().getString())) {
            FishOnMCExtras.LOGGER.info("[FoE] Tracking {}", stack.getName().getString());

            if(config.fishTracker.fishTrackerToggles.otherToggles.useNewTitle) {
                this.sendToTitleHud(fish, this.title, this.subtitle);
            }

            ProfileDataHandler.instance().updateStatsOnCatch(fish);
            ProfileDataHandler.instance().updateStatsOnCatch();
            QuestHandler.instance().updateQuest(fish);
            PetEquipHandler.instance().updatePet(minecraftClient.player);

            if (config.contestTracker.shouldShowFullContest() && config.contestTracker.refreshOnContestPB) {
                // Check if caught fish is for contest and refresh if it's heavier
                var typecheck = ContestHandler.instance().type.replace("Heaviest", "").trim().toLowerCase();
                ContestHandler contestHandler = ContestHandler.instance();
                
                // Check if we are in the right location for the contest
                boolean locationMatches = Objects.equals(
                    Objects.requireNonNull(Constant.valueOfTag(contestHandler.location)) == Constant.SPAWNHUB 
                        ? Constant.CYPRESS_LAKE.ID 
                        : Objects.requireNonNull(Constant.valueOfTag(contestHandler.location).ID), 
                    BossBarHandler.instance().currentLocation.ID
                );
                
                if (contestHandler.isContest && typecheck.contains(fish.groupId.toLowerCase())
                        && locationMatches && (fish.weight > contestHandler.biggestFish)) {
                    ContestHandler.instance().biggestFish = fish.weight;
                    ContestHandler.instance().setRefreshReason("personal_best");
                    minecraftClient.player.networkHandler.sendChatCommand("contest");
                    
                    // Send packet to notify other players of contest PB
                    if(config.contestTracker.recieveLocalPBs) {
                        PacketHandler.CONTEST_PB_PACKET.sendContestPBPacket(fish.groupId, minecraftClient.player.getName().getString(), fish.weight, ScoreboardHandler.instance().level);
                    }
                    
                    FishOnMCExtras.LOGGER.info("[FoE] Refreshed Contest Stats - New heaviest fish: {} lbs",
                            fish.weight);
                }
            }

            this.fishFound = false;
            this.isFull = false;
            this.updateTrackedFish(minecraftClient.player);
            this.title = Text.empty();
            this.subtitle = Text.empty();
            
        }
        
    }

    private void updateTrackedFish(PlayerEntity player) {
        trackFishList.clear();
        for (int i = player.getInventory().main.size() - 1; i >= 0; i--) {
            ItemStack stack = player.getInventory().main.get(i);

            if(stack.isEmpty()) {
                continue;
            }

            Fish fish = Fish.getFish(stack);
            if (fish != null
                    && Objects.equals(fish.catcher, player.getUuid())
                    && !trackFishList.contains(fish.id)) {
                trackFishList.add(fish.id);
            }
        }
    }

    private void sendToTitleHud(Fish fish, Text icon, Text name) {
        // Send to TitleHud
        List<Text> title = new ArrayList<>();
        title.add(icon.copy().formatted(Formatting.WHITE));
        title.add(Text.empty());
        title.add(name);
        title.add(fish.size.TAG);
        if(FullInventoryHandler.instance().slotsLeft == 0) {
            title.add(Text.literal("Inventory Full!").formatted(Formatting.RED));
        }
        List<Text> subtitle = new ArrayList<>();
        if(config.fishTracker.fishTrackerToggles.otherToggles.showStatsOnCatch) {
            subtitle.add(Text.literal("ᴡᴇɪɢʜᴛ").formatted(Formatting.BOLD).withColor(0xFFFFFF));
            subtitle.add(TextHelper.concat(
                    Text.literal(TextHelper.fmt(fish.weight, 2)),
                    Text.literal("ʟʙ").withColor(0xAAAAAA),
                    Text.literal(" (").withColor(0x555555),
                    Text.literal(TextHelper.fmt(fish.weight * 0.453592f, 2)),
                    Text.literal("ᴋɢ").withColor(0xAAAAAA),
                    Text.literal(")").withColor(0x555555)
            ).withColor(0xFFFFFF));
            subtitle.add(Text.literal("ʟᴇɴɢᴛʜ").formatted(Formatting.BOLD).withColor(0xFFFFFF));
            subtitle.add(TextHelper.concat(
                    Text.literal(TextHelper.fmt(fish.length, 2)),
                    Text.literal("ɪɴ").withColor(0xAAAAAA),
                    Text.literal(" (").withColor(0x555555),
                    Text.literal(TextHelper.fmt(fish.length * 2.54f, 2)),
                    Text.literal("ᴄᴍ").withColor(0xAAAAAA),
                    Text.literal(")").withColor(0x555555)
            ).withColor(0xFFFFFF));
        }

        TitleHandler.instance().setTitleHud(title, config.fishTracker.fishTrackerToggles.otherToggles.showStatsOnCatchTime * 1000L, MinecraftClient.getInstance(), subtitle);
    }

    public void onFishCaughtSendDryStreak(Fish fish) {
        if (fish.rarity == Constant.COMMON && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.rarityMessageToggles.showCommon ||
                fish.rarity == Constant.RARE && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.rarityMessageToggles.showRare ||
                fish.rarity == Constant.EPIC && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.rarityMessageToggles.showEpic ||
                fish.rarity == Constant.LEGENDARY && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.rarityMessageToggles.showLegendary ||
                fish.rarity == Constant.MYTHICAL && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.rarityMessageToggles.showMythical) {

            sendFishDryStreakMessage(fish.rarity,
                    ProfileDataHandler.instance().profileData.rarityDryStreak.getOrDefault(fish.rarity, 0));
        }

        if (fish.size == Constant.BABY && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.sizeMessageToggles.showBaby ||
                fish.size == Constant.JUVENILE && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.sizeMessageToggles.showJuvenile ||
                fish.size == Constant.ADULT && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.sizeMessageToggles.showAdult ||
                fish.size == Constant.LARGE && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.sizeMessageToggles.showLarge ||
                fish.size == Constant.GIGANTIC && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.sizeMessageToggles.showGigantic) {

            sendFishDryStreakMessage(fish.size,
                    ProfileDataHandler.instance().profileData.fishSizeDryStreak.getOrDefault(fish.size, 0));
        }

        if (fish.variant == Constant.ALBINO && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.variantMessageToggles.showAlbino ||
                fish.variant == Constant.MELANISTIC && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.variantMessageToggles.showMelanistic ||
                fish.variant == Constant.TROPHY && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.variantMessageToggles.showTrophy ||
                fish.variant == Constant.FABLED && config.fishTracker.fishTrackerToggles.dryStreakMessageToggles.variantMessageToggles.showFabled) {

            sendFishDryStreakMessage(fish.variant,
                    ProfileDataHandler.instance().profileData.variantDryStreak.getOrDefault(fish.variant, 0));
        }
    }

    private void sendFishDryStreakMessage(Constant fish, int lastCaught) {
        String article = (fish == Constant.EPIC || fish == Constant.ADULT || fish == Constant.ALBINO) ? "an " : "a ";
        sendDryStreakMessage(fish.TAG, article, lastCaught);
    }

    private void sendItemDryStreakMessage(String item, int lastCaught) {
        Text itemText;
        if (item.equals("pet")) {
            itemText = Text.literal("Pet").withColor(0xFD95F6);
        } else if (item.equals("shard")) {
            itemText = Text.literal("Shard").formatted(Formatting.GOLD);
        } else {
            itemText = Text.literal("Lightning Bottle").formatted(Formatting.YELLOW);
        }
        sendDryStreakMessage(itemText, "a ", lastCaught);
    }

    private void sendDryStreakMessage(Text typeText, String article, int lastCaught) {
        int dryAmount = Math.max(0, ProfileDataHandler.instance().profileData.allFishCaughtCount - lastCaught - 1);
        var client = MinecraftClient.getInstance();

        if (client.player != null) {
            client.inGameHud.getChatHud().addMessage(TextHelper.concat(
                    Text.literal("FOE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                    Text.literal("» ").formatted(Formatting.DARK_GRAY),
                    Text.literal("You went ").formatted(Formatting.GRAY),
                    Text.literal(TextHelper.fmnt(dryAmount)).formatted(Formatting.YELLOW),
                    Text.literal(" fish dry before catching " + article).formatted(Formatting.GRAY),
                    typeText
            ));
        }
    }
}
