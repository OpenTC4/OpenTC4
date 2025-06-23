package com.opentc4.core;

import com.opentc4.api.OTC4API;
import com.opentc4.research.ResearchManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = OpenTC4.MODID,
    name = OpenTC4.NAME,
    version = OpenTC4.VERSION,
    dependencies = "required-after:Forge@[10.13.2,);required-after:Baubles@[1.0.1.10,)")

public class OpenTC4 {

    public static final String MODID = "opentc4";
    public static final String NAME = "OpenTC4";
    public static final String VERSION = "0.1.0-alpha";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Setup config, API bridges, and early registries
        OTC4API.researchHandler = new ResearchManager();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register things like items, blocks, GUIs
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Cross-mod compatibility, recipes, final hooks
    }
}
