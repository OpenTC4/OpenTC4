package com.opentc4.client;

import net.minecraftforge.client.MinecraftForgeClient;

import com.opentc4.ModItems;
import com.opentc4.client.render.item.ItemThaumometerRenderer;
import com.opentc4.common.CommonProxy;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenders() {
        MinecraftForgeClient.registerItemRenderer(ModItems.itemThaumometer, new ItemThaumometerRenderer());
    }
}
