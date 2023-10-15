package com.stevekung.squidnoglitch.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.phys.Vec3;

@Mixin(Squid.class)
public class MixinSquid extends WaterAnimal
{
    MixinSquid()
    {
        super(null, null);
    }

    /**
     * Fix for <a href="https://bugs.mojang.com/browse/MC-39263">MC-39263</a>, <a href="https://bugs.mojang.com/browse/MC-58294">MC-58294</a>, <a href="https://bugs.mojang.com/browse/MC-89883">MC-89883</a>, <a href="https://bugs.mojang.com/browse/MC-136421">MC-136421</a>, <a href="https://bugs.mojang.com/browse/MC-212213">MC-212213</a>, <a href="https://bugs.mojang.com/browse/MC-225422">MC-225422</a>
     * and partically fix <a href="https://bugs.mojang.com/browse/MC-134626">MC-134626</a>
     *
     * <p> Add {@link net.minecraft.world.entity.LivingEntity#isEffectiveAi()} check to prevent movement de-sync on the server.</p>
     */
    @Override
    public void travel(Vec3 travelVector)
    {
        if (this.isEffectiveAi())
        {
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
    }

    /**
     * <p>Fix Levitation doesn't apply reset fall distance, no bug reported yet.</p>
     */
    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/animal/Squid.getEffect(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/world/effect/MobEffectInstance;"))
    private void squidnoglitch$resetFallDistanceForLevitation(CallbackInfo info)
    {
        this.resetFallDistance();
    }

    /**
     * Fix for <a href="https://bugs.mojang.com/browse/MC-167008">MC-167008</a>
     *
     * <p>Code taken from <a href="https://bugs.mojang.com/browse/MC-167008">MC-167008</a> and credit to <a href="https://bugs.mojang.com/secure/ViewProfile.jspa?name=Thumpbacker">Thumpbacker</a></p>
     *
     * <p>Add Slow Falling movement vector to squids. And reset fall distance.</p>
     */
    @ModifyVariable(method = "aiStep", at = @At(value = "STORE", ordinal = 0), slice = @Slice(from = @At(value = "INVOKE", target = "net/minecraft/world/entity/animal/Squid.getDeltaMovement()Lnet/minecraft/world/phys/Vec3;", ordinal = 1)), index = 1, ordinal = 0)
    private double squidnoglitch$addSlowFallingSpeed(double defaultValue)
    {
        if (this.hasEffect(MobEffects.SLOW_FALLING))
        {
            this.resetFallDistance();
            return -0.05D * (double) (this.getEffect(MobEffects.SLOW_FALLING).getAmplifier() + 1);
        }
        return defaultValue;
    }

    /**
     * Fix for <a href="https://bugs.mojang.com/browse/MC-132473">MC-132473</a>
     *
     * <p>Decreasing movement vector for Y to {@code 0.15f} should stop squids become stuck on land.</p>
     */
    @Mixin(targets = "net.minecraft.world.entity.animal.Squid$SquidRandomMovementGoal", priority = 1001)
    public static class SquidRandomMovementGoal_MC132473
    {
        @ModifyConstant(method = "tick", constant = @Constant(floatValue = 0.2F, ordinal = 1))
        private float squidnoglitch$modifyYMovementSpeed(float oldSpeed)
        {
            return 0.15F;
        }
    }

    /**
     * Fix <a href="https://bugs.mojang.com/browse/MC-212687">MC-212687</a>
     *
     * <p> Removing {@link net.minecraft.world.entity.LivingEntity#getNoActionTime()} check will restore the movement of squid if the player is far from them.</p>
     *
     * <p> FYI: I'm not sure what is a Mojang standard for mobs that are far from the player. Since Dolphins and Turtles doesn't freeze their movement when the player is far from them. </p>
     */
    @Mixin(targets = "net.minecraft.world.entity.animal.Squid$SquidRandomMovementGoal")
    public abstract static class SquidRandomMovementGoal_MC212687 extends Goal
    {
        @Shadow
        @Final
        Squid squid;

        @Override
        public void tick()
        {
            if (this.squid.getRandom().nextInt(reducedTickDelay(50)) == 0 || !this.squid.isInWater() || !this.squid.hasMovementVector())
            {
                var f = this.squid.getRandom().nextFloat() * (float) (Math.PI * 2);
                var tx = Mth.cos(f) * 0.2F;
                var ty = -0.1F + this.squid.getRandom().nextFloat() * 0.2F; // This constant will be replaced by `SquidRandomMovementGoal_MC132473`
                var tz = Mth.sin(f) * 0.2F;
                this.squid.setMovementVector(tx, ty, tz);
            }
        }
    }
}