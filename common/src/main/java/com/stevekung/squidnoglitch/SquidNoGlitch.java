package com.stevekung.squidnoglitch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SquidNoGlitch
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static void init()
    {
        LOGGER.info("SquidNoGlitch loaded, No more squids glitch through the block!");
    }
}