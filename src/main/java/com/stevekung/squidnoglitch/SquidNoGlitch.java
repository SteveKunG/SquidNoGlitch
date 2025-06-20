package com.stevekung.squidnoglitch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "squidnoglitch", name = "SquidNoGlitch", version = "1.0.4")
public class SquidNoGlitch
{
    public static final Logger LOGGER = LogManager.getLogger("SquidNoGlitch");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER.info("SquidNoGlitch loaded, No more squids glitch through the block!");
    }
}