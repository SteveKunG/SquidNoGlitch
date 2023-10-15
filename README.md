# SquidNoGlitch
A tiny mod that fixes various vanilla bugs with the squid

Support with Minecraft 1.18.2-1.20.x for Fabric (Quilt maybe?)/Forge

Download on [Modrinth](https://modrinth.com/mod/squid-no-glitch)/[CurseForge](https://curseforge.com/minecraft/mc-mods/squid-no-glitch)

### List of bug fixes for squid
- Adding check to prevent movement de-sync on the server. Which resolves [MC-39263](https://bugs.mojang.com/browse/MC-39263), [MC-58294](https://bugs.mojang.com/browse/MC-58294), [MC-89883](https://bugs.mojang.com/browse/MC-89883), [MC-136421](https://bugs.mojang.com/browse/MC-136421), [MC-212213](https://bugs.mojang.com/browse/MC-212213), [MC-225422](https://bugs.mojang.com/browse/MC-225422) and partially fixes [MC-134626](https://bugs.mojang.com/browse/MC-134626)
- Fix Levitation effect doesn't apply reset fall distance, no bug reported yet.
- Adding Slow Falling effect movement vector to squids. And reset fall distance. [MC-167008](https://bugs.mojang.com/browse/MC-167008).
- Decreasing Y movement vector to `0.15f` which should stop squids become stuck on land. [MC-132473](https://bugs.mojang.com/browse/MC-132473).
- Removing `getNoActionTime()` check will restore the movement of squid if the player is far from them. [MC-212687](https://bugs.mojang.com/browse/MC-212687).

### Installation
- Install Fabric Loader/MinecraftForge
- Copy mod into `mods` folder