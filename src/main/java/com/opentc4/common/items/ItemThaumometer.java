package com.opentc4.common.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.opentc4.OpenTC4;

public class ItemThaumometer extends Item {

    public ItemThaumometer() {
        setMaxStackSize(1);
        setCreativeTab(OpenTC4.tabOpenTC4);
        setTextureName("opentc4:nuggetiron");
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }
}
