package com.opentc4;

import net.minecraft.item.Item;

import com.opentc4.items.ItemThaumonomicon;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

    public static Item itemThaumonomicon;

    public static void init() {
        itemThaumonomicon = new ItemThaumonomicon().setUnlocalizedName("itemThaumonomicon");
    }

    public static void registerItems() {
        GameRegistry.registerItem(ModItems.itemThaumonomicon, "itemThaumonomicon");
    }
}
