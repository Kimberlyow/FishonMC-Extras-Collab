package io.github.markassk.fishonmcextras.mixin;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets={"net.minecraft.class_338"}, priority=9999)
public class ChatFilterMixin {
    @Unique
    private Object pendingBlankMessage = null;
    @Unique
    private boolean isReplaying = false;
    @Unique
    private boolean killNextBlank = false;

    @Inject(method={"method_1812"}, at={@At(value="HEAD")}, cancellable=true, remap=false)
    private void onAddMessage(@Coerce Object message, CallbackInfo ci) {
        if (!FishOnMCExtrasConfig.getConfig().chatFilter.enabled) {
            return;
        }
        
        try {
        	
            if (this.isReplaying) {
                return;
            }
            String rawContent = this.getTextSafe(message);
            boolean isBlank = this.isActuallyBlank(rawContent);
            if (this.killNextBlank) {
                if (isBlank) {
                    System.out.println("[ChatFilter] Blocked bottom blank.");
                    ci.cancel();
                    this.killNextBlank = false;
                    return;
                }
                this.killNextBlank = false;
            }
            if (this.isTargetMessage(rawContent)) {
                System.out.println("[ChatFilter] BLOCKED SPAM: " + rawContent);
                ci.cancel();
                if (this.pendingBlankMessage != null) {
                    System.out.println("[ChatFilter] Blocked top blank.");
                    this.pendingBlankMessage = null;
                }
                this.killNextBlank = true;
                return;
            }
            if (isBlank) {
                if (this.pendingBlankMessage != null) {
                    this.releasePendingBlank();
                }
                this.pendingBlankMessage = message;
                this.scheduleTimeoutRelease(message);
                ci.cancel();
                return;
            }
            if (this.pendingBlankMessage != null) {
                this.releasePendingBlank();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Unique
    private void scheduleTimeoutRelease(Object targetMessage) {
        new Thread(() -> {
            try {
                Thread.sleep(200L);
                if (this.pendingBlankMessage == targetMessage) {
                    this.runOnMainThread(() -> {
                        if (this.pendingBlankMessage == targetMessage) {
                            this.releasePendingBlank();
                        }
                    });
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Unique
    private void runOnMainThread(Runnable action) {
        try {
            Class<?> mcClass = Class.forName("net.minecraft.class_310");
            Method getInstance = mcClass.getMethod("method_1551", new Class[0]);
            Object mc = getInstance.invoke(null, new Object[0]);
            for (Method m : mcClass.getMethods()) {
                if (m.getParameterCount() != 1 || m.getParameterTypes()[0] != Runnable.class) continue;
                m.invoke(mc, action);
                return;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Unique
    private void releasePendingBlank() {
        if (this.pendingBlankMessage == null) {
            return;
        }
        Object msgToRelease = this.pendingBlankMessage;
        this.pendingBlankMessage = null;
        this.isReplaying = true;
        try {
            Method targetMethod = null;
            for (Method m : this.getClass().getMethods()) {
                if (!m.getName().equals("method_1812") && !m.getName().equals("addMessage") || m.getParameterCount() != 1) continue;
                targetMethod = m;
                break;
            }
            if (targetMethod != null) {
                targetMethod.invoke((Object)this, msgToRelease);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.isReplaying = false;
    }

    private String getTextSafe(Object textObj) {
        if (textObj == null) {
            return "";
        }
        try {
            Method[] methods = textObj.getClass().getMethods();
            for (Method method : methods) {
                Object res;
                if (!method.getName().equals("getString") && !method.getName().equals("method_10851") && !method.getName().equals("asString") || !((res = method.invoke(textObj, new Object[0])) instanceof String)) continue;
                return (String)res;
            }
            for (AccessibleObject accessibleObject : textObj.getClass().getDeclaredFields()) {
                ((Field)accessibleObject).setAccessible(true);
                Object val = ((Field)accessibleObject).get(textObj);
                if (!(val instanceof String) || ((String)val).isEmpty()) continue;
                return (String)val;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return textObj.toString();
    }

    private boolean isActuallyBlank(String text) {
        if (text == null) {
            return true;
        }
        if (text.startsWith("class_") || text.contains("$$Lambda") || text.contains("net.minecraft")) {
            return true;
        }
        String clean = text.replaceAll("\u00a7.", "").replaceAll("\\[.*?\\]", "").replaceAll("\\<.*?\\>", "").replaceAll("[^\\x21-\\x7E]", "");
        return clean.isEmpty();
    }

    private boolean isTargetMessage(String text) {
        if (text == null) {
            return false;
        }
        return text.contains("DESIGN TEAM \u00bb") || text.contains("HELP \u00bb") || text.contains("WIKI \u00bb") || text.contains("RULES \u00bb") || text.contains("BUILD TEAM \u00bb") || text.contains("COSMETICS \u00bb") || text.contains("STORE \u00bb") || text.contains("DISCORD \u00bb");
    }
}