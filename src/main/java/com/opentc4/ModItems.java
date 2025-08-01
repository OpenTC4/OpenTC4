package com.opentc4;

import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

import com.opentc4.common.items.ItemThaumometer;
import com.opentc4.common.items.ItemThaumonomicon;
import com.opentc4.common.items.NuggetIron;
import com.opentc4.common.items.WandCapIron;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

    public static Item itemThaumonomicon;
    public static Item nuggetIron;
    public static Item wandCapIron;
    public static Item itemThaumometer;

    public static void init() {
        itemThaumonomicon = new ItemThaumonomicon().setUnlocalizedName("itemThaumonomicon");
        nuggetIron = new NuggetIron().setUnlocalizedName("nuggetIron");
        wandCapIron = new WandCapIron().setUnlocalizedName("wandCapIron");
        itemThaumometer = new ItemThaumometer().setUnlocalizedName("itemThaumometer");
    }

    public static void registerItems() {
        GameRegistry.registerItem(ModItems.itemThaumonomicon, "itemThaumonomicon");
        GameRegistry.registerItem(ModItems.nuggetIron, "nuggetIron");
        GameRegistry.registerItem(ModItems.wandCapIron, "wandCapIron");
        GameRegistry.registerItem(ModItems.itemThaumometer, "itemThaumometer");
    }

    public static void registerDictionary() {
        OreDictionary.registerOre("nuggetIron", nuggetIron);
    }
}
