package com.stevekung.squidnoglitch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.stevekung.squidnoglitch.core.Tags;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies = SquidNoGlitch.MAIN_DEPENDENCIES)
public class SquidNoGlitch
{
    public static final Logger LOGGER = LogManager.getLogger("SquidNoGlitch");
    protected static final String MAIN_DEPENDENCIES = "required-after:mixinbooter@[" + Tags.MIXIN_BOOTER_VERSION + ",); ";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER.info("SquidNoGlitch loaded, No more squids glitch through the block!");
    }
}