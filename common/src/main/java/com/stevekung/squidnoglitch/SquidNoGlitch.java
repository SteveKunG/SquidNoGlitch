package com.stevekung.squidnoglitch;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class SquidNoGlitch
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void init()
    {
        LOGGER.info("SquidNoGlitch loaded, No more squids glitch through the block!");
    }
}