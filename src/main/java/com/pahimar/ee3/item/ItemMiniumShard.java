package com.pahimar.ee3.item;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.api.exchange.IEnergyValueProvider;
import com.pahimar.ee3.reference.Names;
import net.minecraft.item.ItemStack;

public class ItemMiniumShard extends ItemEE implements IEnergyValueProvider {
    public ItemMiniumShard() {
        super(Names.Items.MINIUM_SHARD);
        this.setMaxStackSize(64);
    }

    @Override
    public EnergyValue getEnergyValue(ItemStack itemStack) {
        if (itemStack != null && itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(Names.NBT.ENERGY_VALUE)) {
            if (Float.compare(itemStack.getTagCompound().getFloat(Names.NBT.ENERGY_VALUE), 0) > 0) {
                return new EnergyValue(itemStack.getTagCompound().getFloat(Names.NBT.ENERGY_VALUE));
            }
        }
        return null;
    }
}
