package com.opentc4;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {

    public static void init() {
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.nuggetIron, 9), "ingotIron"));
        GameRegistry
            .addRecipe(new ShapedOreRecipe(new ItemStack(Items.iron_ingot, 1), "III", "III", "III", 'I', "nuggetIron"));
    }
}
