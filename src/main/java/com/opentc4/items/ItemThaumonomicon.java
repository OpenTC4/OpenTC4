package com.opentc4.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.opentc4.OpenTC4;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemThaumonomicon extends Item {

    public ItemThaumonomicon() {
        setMaxStackSize(1);
        setCreativeTab(OpenTC4.tabOpenTC4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("opentc4:itemthaumonomicon");
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }
}
