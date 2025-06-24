package com.opentc4;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {

    public static void init() {
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.nuggetIron, 9), new ItemStack(Items.iron_ingot));
        GameRegistry.addRecipe(
            new ItemStack(Items.iron_ingot, 1),
            new Object[] { "III", "III", "III", 'I', ModItems.nuggetIron });
    }
}
