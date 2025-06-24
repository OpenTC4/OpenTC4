package com.opentc4;
//every single item
import com.opentc4.common.items.ItemThaumonomicon;
import com.opentc4.common.items.NuggetIron;
import com.opentc4.common.items.WandCapIron;
// the actual imports
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class ModItems {

    public static Item itemThaumonomicon;
    public static Item nuggetIron;
    public static Item wandCapIron;

    public static void init() {
        itemThaumonomicon = new ItemThaumonomicon().setUnlocalizedName("itemThaumonomicon");
        nuggetIron = new NuggetIron().setUnlocalizedName("nuggetIron");
        wandCapIron = new WandCapIron().setUnlocalizedName("wandCapIron");
    }

    public static void registerItems() {
        GameRegistry.registerItem(ModItems.itemThaumonomicon, "itemThaumonomicon");
        GameRegistry.registerItem(ModItems.nuggetIron, "nuggetIron");
        GameRegistry.registerItem(ModItems.wandCapIron, "wandCapIron");
    }

    public static void registerDictionary() {
        OreDictionary.registerOre("nuggetIron", nuggetIron);
    }
}
