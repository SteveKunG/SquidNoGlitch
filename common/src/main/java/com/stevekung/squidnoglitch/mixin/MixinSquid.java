package com.stevekung.squidnoglitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.squid.Squid;

@Mixin(Squid.class)
public abstract class MixinSquid extends AgeableWaterCreature
{
    MixinSquid()
    {
        super(null, null);
    }

    /**
     * <p>Fix Levitation effect doesn't apply reset fall distance, no bug reported yet.</p>
     */
    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/animal/squid/Squid.getEffect(Lnet/minecraft/core/Holder;)Lnet/minecraft/world/effect/MobEffectInstance;"))
    private void squidnoglitch$resetFallDistanceForLevitation(CallbackInfo info)
    {
        this.resetFallDistance();
    }

    /**
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-167008">MC-167008</a></p>
     *
     * <p>Code taken from <a href="https://bugs.mojang.com/browse/MC-167008">MC-167008</a> and credit to <a href="https://bugs.mojang.com/secure/ViewProfile.jspa?name=Thumpbacker">Thumpbacker</a></p>
     *
     * <p>Adding Slow Falling effect movement vector to squids. And reset fall distance.</p>
     */
    @ModifyVariable(method = "aiStep", at = @At(value = "STORE", ordinal = 0), slice = @Slice(from = @At(value = "FIELD", target = "net/minecraft/world/phys/Vec3.y:D", ordinal = 1)), index = 1, ordinal = 0)
    private double squidnoglitch$addSlowFallingSpeed(double defaultValue)
    {
        if (this.hasEffect(MobEffects.SLOW_FALLING))
        {
            this.resetFallDistance();
            return -0.05D * (this.getEffect(MobEffects.SLOW_FALLING).getAmplifier() + 1);
        }
        return defaultValue;
    }

    /**
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-132473">MC-132473</a></p>
     *
     * <p>Decreasing Y movement vector to {@code 0.15f} which should stop squids become stuck on land.</p>
     */
    @Mixin(targets = "net.minecraft.world.entity.animal.squid.Squid$SquidRandomMovementGoal")
    public static class SquidRandomMovementGoal_MC132473
    {
        @ModifyConstant(method = "tick", constant = @Constant(floatValue = 0.2F, ordinal = 1))
        private float squidnoglitch$modifyYMovementSpeed(float oldSpeed)
        {
            return 0.175f;
        }
    }

    /**
     * <p>Fix <a href="https://bugs.mojang.com/browse/MC-212687">MC-212687</a></p>
     *
     * <p>Removing {@link net.minecraft.world.entity.LivingEntity#getNoActionTime()} check will restore the movement of squid if the player is far from them.</p>
     *
     * <p>FYI: I'm not sure what is a Mojang standard for mobs that are far from the player. Since Dolphins and Turtles doesn't freeze their movement when the player is far from them.</p>
     */
    @Mixin(targets = "net.minecraft.world.entity.animal.squid.Squid$SquidRandomMovementGoal")
    public static class SquidRandomMovementGoal_MC212687
    {
        @Definition(id = "i", local = @Local(type = int.class))
        @Expression("i > 100")
        @ModifyExpressionValue(method = "tick", at = @At("MIXINEXTRAS:EXPRESSION"))
        private boolean skipNoActionTime(boolean original)
        {
            return false;
        }
    }
}