package com.stevekung.squidnoglitch;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import io.github.tox1cozz.mixinbooterlegacy.IEarlyMixinLoader;

import javax.annotation.Nullable;

@IFMLLoadingPlugin.MCVersion("1.7.10")
public class SquidNoGlitchPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader
{
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }

    @Override
    public List<String> getMixinConfigs()
    {
        return ImmutableList.of("mixins.squidnoglitch.json");
    }
}