# FishNoStuck
A small mod that fixes fish stop moving entirely [MC-182763](https://bugs.mojang.com/browse/MC-182763)

Support with Minecraft 1.17.x-1.20.x for Fabric (Quilt maybe?)/Forge

Download on [Modrinth](https://modrinth.com/mod/fish-no-stuck)/[CurseForge](https://legacy.curseforge.com/minecraft/mc-mods/fish-no-stuck)

### How it works
The fish has an AI Goal that scans for the nearest fish, which will randomly select a leader and followers. Then it will let the follower fish follow the leader.

This mod applies bug fix to `FollowFlockLeaderGoal` by checking the nearest fish if it is a leader or not.
If not, it will add into the follower list.

If leader gets added into the follower list.
A Fish Leader will stop and not go to anywhere along with their followers (Forever!).
Unless you hit them or switch gamemode to survival and swim near them.

For Minecraft Bug Report, see: [https://bugs.mojang.com/browse/MC-182763](https://bugs.mojang.com/browse/MC-182763)

### Another fix in this mod is:
- Check entity predicate by using `EntitySelector.NO_CREATIVE_OR_SPECTATOR` instead of `EntitySelector.NO_SPECTATORS` in avoid entity goal.
- Set `checkNoActionTime` to `false` in `FishSwimGoal` to fix fish getting stuck at the current position
- Decreased `schoolSize` from leader fish if the follower fish is getting killed.

### Performance Impact
This mod doesn't improve any game performance, or it has very minor impact, especially for fish.
See [FishNoStuck#1](https://github.com/SteveKunG/FishNoStuck/issues/1) 

### Installation
- Install Fabric Loader/MinecraftForge
- Copy mod into `mods` folder

### Backstory
I was working on [Fish of Thieves](https://github.com/SteveKunG/FishOfThieves) by looking at `FollowFlockLeaderGoal` and figure it out how it works, then convert it to use `Brain` system.

By display debug data above an entity.

![](https://cdn.modrinth.com/data/IjXg2Day/images/21c44584b9c6c60bd24107b39b515c5fcef714c9.png)

Then I realize something wrong. A fish with `schoolSize=8` has property `isFollower=true` and has a leader. Which it should not be possible.
So that's the beginning of this mod.

![](https://cdn.modrinth.com/data/IjXg2Day/images/1f3bca5e9f61c8f992243382579363530aea9e5b.png)