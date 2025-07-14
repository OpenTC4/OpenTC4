package com.opentc4.common.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.opentc4.OpenTC4;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemThaumonomicon extends Item implements IGuiHolder<GuiData> {

    public ItemThaumonomicon() {
        setMaxStackSize(1);
        setMaxDamage(0);
        setCreativeTab(OpenTC4.tabOpenTC4);
    }

    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager, UISettings settings) {
        settings.getNEISettings()
            .disableNEI();
        return new ModularPanel("opentc4:thaumonomicon").child(
            IKey.str("Hello")
                .asWidget()
                .center());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("opentc4:thaumonomicon");
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            GuiFactories.item()
                .open(player);
        }
        return super.onItemRightClick(stack, world, player);
    }
}
