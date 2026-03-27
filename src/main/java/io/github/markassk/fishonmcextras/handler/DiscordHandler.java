package io.github.markassk.fishonmcextras.handler;

import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.*;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.util.ExtendedRichPresence;
import net.minecraft.client.MinecraftClient;

public class DiscordHandler {
    private static DiscordHandler INSTANCE = new DiscordHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
    private IPCClient ipcClient;
    private RichPresence currentRichPresence;
    private long offsetTime;
    private boolean shouldConnect = true;
    
    // Cached values to detect changes
    private String cachedState = "";
    private String cachedDetails = "";
    private int cachedLevel = -1;
    private String cachedLocation = "";

    public static DiscordHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new DiscordHandler();
        }
        return INSTANCE;
    }

    public void tick() {
        if(!shouldConnect) return;
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        String location = BossBarHandler.instance().currentLocation.TAG.getString();
        String instance = TabHandler.instance().instance;
        boolean isInstance = TabHandler.instance().isInstance;
        boolean isVanished = StaffHandler.instance().isVanished;
        
        String newState;
        if (isInstance) {
            newState = "Fishing at: " 
                + (isVanished ? Constant.CYPRESS_LAKE.TAG.getString() : location)
                + " (i" + (isVanished ? "1" : instance) + ")";
        } else {
            newState = "At: " + location;
        }
        
        int newLevel = client.player.experienceLevel;
        String newDetails = client.player.getName().getString() + " [" + newLevel + "]";
        
        if (!newState.equals(cachedState) || !newDetails.equals(cachedDetails)) {
            cachedState = newState;
            cachedDetails = newDetails;
            cachedLevel = newLevel;
            cachedLocation = location;
            
            currentRichPresence = new ExtendedRichPresence.ExtendedBuilder()
                    .setActivity(ActivityType.Playing)
                    .setState(newState)
                    .setStartTimestamp(offsetTime)
                    .setLargeImage("small_logo")
                    .setDetails(newDetails)
                    .build();
            
            this.processState();
        }
    }

    public void init() {
        if(this.ipcClient == null && shouldConnect) {
            this.ipcClient = new IPCClient(config.discordIPC.clientId);

            this.offsetTime = System.currentTimeMillis();

            this.ipcClient.setListener(new IPCListener() {
                @Override
                public void onPacketSent(IPCClient ipcClient, Packet packet) {

                }

                @Override
                public void onPacketReceived(IPCClient ipcClient, Packet packet) {

                }

                @Override
                public void onActivityJoin(IPCClient ipcClient, String s) {

                }

                @Override
                public void onActivitySpectate(IPCClient ipcClient, String s) {

                }

                @Override
                public void onActivityJoinRequest(IPCClient ipcClient, String s, User user) {

                }

                @Override
                public void onReady(IPCClient client) {
                    FishOnMCExtras.LOGGER.info("Discord client ready");
                }

                @Override
                public void onClose(IPCClient ipcClient, JsonObject jsonObject) {

                }

                @Override
                public void onDisconnect(IPCClient ipcClient, Throwable throwable) {

                }
            });

            this.currentRichPresence = new ExtendedRichPresence.ExtendedBuilder()
                    .setState("Loading")
                    .build();
        }
    }

    public void connect() {
        if(this.ipcClient.getStatus() != PipeStatus.CONNECTED && config.discordIPC.isEnabled && shouldConnect) {
            try {
                this.ipcClient.connect();
            } catch (NoDiscordClientException e) {
                FishOnMCExtras.LOGGER.error("Unable to connect to the discord client", e);
            } catch (NoClassDefFoundError e) {
                FishOnMCExtras.LOGGER.error("Could not find class", e);
                shouldConnect = false;
            } catch (Throwable e) {
                FishOnMCExtras.LOGGER.error("Other issue: ", e);
            }
        }
    }

    public void disconnect() {
        if(shouldConnect && ipcClient != null && ipcClient.getStatus() == PipeStatus.CONNECTED) {
            ipcClient.close();
        }
    }

    private void processState() {
        if(this.ipcClient == null || this.ipcClient.getStatus() != PipeStatus.CONNECTED) {
            return;
        }


        this.ipcClient.sendRichPresence(currentRichPresence, new Callback((success) -> {}, (error) -> FishOnMCExtras.LOGGER.error("Failed to send state to discord: {}", error)));
    }

}
