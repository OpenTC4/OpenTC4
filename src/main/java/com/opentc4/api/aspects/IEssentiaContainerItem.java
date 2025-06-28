package com.opentc4.api.aspects;

import net.minecraft.item.ItemStack;

public interface IEssentiaContainerItem {

    AspectList getAspects(ItemStack var1);

    void setAspects(ItemStack var1, AspectList var2);
}
