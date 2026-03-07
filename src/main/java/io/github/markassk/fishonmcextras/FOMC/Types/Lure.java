package io.github.markassk.fishonmcextras.FOMC.Types;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lure extends FOMCItem {
    public final String name;
    public final CustomModelDataComponent customModelData;
    public final int totalUses;
    public final int counter;
    public final Constant water;
    public final String intricacy;
    public final List<LureStats> lureStats;
    public final String size;
    public final String color;

    private Lure(NbtCompound nbtCompound, String type, CustomModelDataComponent customModelData) {
        super(type, Constant.valueOfId(nbtCompound.getString("rarity")));
        this.name = nbtCompound.getString("name");
        this.customModelData = customModelData;
        this.counter = nbtCompound.getInt("counter");
        this.water = Constant.valueOfId(nbtCompound.getString("water"));
        this.intricacy = nbtCompound.getString("intricacy");
        this.color = nbtCompound.getString("color");
        NbtList nbtList = nbtCompound.getList("base", NbtElement.LIST_TYPE);
        List<NbtCompound> nbtCompoundList = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            nbtCompoundList.add(nbtList.getCompound(i));
        }
        this.lureStats = nbtCompoundList.stream().map(LureStats::new).toList();
        this.totalUses = nbtCompound.getInt("totalUses");
        this.size = nbtCompound.getString("size");
    }

    public static class LureStats {
        public final int cur;
        public final String id;

        private LureStats(NbtCompound nbtCompound) {
            this.cur = nbtCompound.getInt("cur");
            this.id = nbtCompound.getString("id");
        }
    }

    public static Lure getLure(ItemStack itemStack, String type) {
        return new Lure(Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)), type,
                itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA));
    }

    public static Lure getLure(ItemStack itemStack) {
        if (itemStack.get(DataComponentTypes.LORE) != null
                && itemStack.get(DataComponentTypes.CUSTOM_DATA) != null
                && !Objects.requireNonNull(ItemStackHelper.getNbt(itemStack)).getBoolean("shopitem")) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if (nbtCompound != null && nbtCompound.contains("type")
                    && Objects.equals(nbtCompound.getString("type"), Defaults.ItemTypes.LURE)) {
                return Lure.getLure(itemStack, Defaults.ItemTypes.LURE);
            }
        }
        return null;
    }

    public int calculateLures(List<FOMCItem> tacklebox) {
        int lureQty = 0;
        String name = this.name;
        String rarity = this.rarity.ID;
        String color = this.color;

        for (FOMCItem entry : tacklebox) {
            if (entry instanceof Lure lure
                    && lure.name.equals(name)
                    && lure.rarity.ID.equals(rarity)
                    && lure.color.equals(color)) {
                lureQty += lure.counter;
                continue;
            }
            break;
        }

        return lureQty;
    }
}
