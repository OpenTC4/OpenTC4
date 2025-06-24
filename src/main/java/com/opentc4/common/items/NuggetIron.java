package com.opentc4.common.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.opentc4.OpenTC4;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NuggetIron extends Item {

    public NuggetIron() {
        setMaxStackSize(64);
        setCreativeTab(OpenTC4.tabOpenTC4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("opentc4:nuggetiron");
    }
}
