plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

minecraft {
    extraRunJvmArguments.add("-Dfml.coreMods.load=com.stevekung.squidnoglitch.SquidNoGlitchPlugin")
}