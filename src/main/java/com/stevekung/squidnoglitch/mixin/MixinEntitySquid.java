package com.stevekung.squidnoglitch.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.util.math.MathHelper;

@Mixin(EntitySquid.class)
public class MixinEntitySquid extends EntityWaterMob
{
    MixinEntitySquid()
    {
        super(null);
    }

    /**
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-39263">MC-39263</a>, <a href="https://bugs.mojang.com/browse/MC-58294">MC-58294</a>, <a href="https://bugs.mojang.com/browse/MC-89883">MC-89883</a>, <a href="https://bugs.mojang.com/browse/MC-136421">MC-136421</a>, <a href="https://bugs.mojang.com/browse/MC-212213">MC-212213</a>, <a href="https://bugs.mojang.com/browse/MC-225422">MC-225422</a>
     * and partially fix <a href="https://bugs.mojang.com/browse/MC-134626">MC-134626</a></p>
     *
     * <p>Add {@link EntityLiving#isServerWorld()} or {@link EntityLiving#canPassengerSteer()} check to prevent movement de-sync on the client.</p>
     */
    @Override
    public void travel(float strafe, float vertical, float forward)
    {
        if (this.isServerWorld() || this.canPassengerSteer())
        {
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        }
    }

    /**
     * <p>Fix Levitation effect doesn't apply reset fall distance, no bug reported yet.</p>
     */
    @Inject(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "net/minecraft/entity/passive/EntitySquid.getActivePotionEffect(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;"))
    private void squidnoglitch$resetFallDistanceForLevitation(CallbackInfo info)
    {
        this.fallDistance = 0.0f;
    }

    /**
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-132473">MC-132473</a></p>
     *
     * <p>Decreasing Y movement vector to {@code 0.15f} which should stop squids become stuck on land.</p>
     */
    @Mixin(targets = "net.minecraft.entity.passive.EntitySquid$AIMoveRandom", priority = 1001)
    public static class AIMoveRandom_MC132473
    {
        @ModifyConstant(method = "updateTask", constant = @Constant(floatValue = 0.2F, ordinal = 1))
        private float squidnoglitch$modifyYMovementSpeed(float oldSpeed)
        {
            return 0.15F;
        }
    }

    /**
     * <p>Fix <a href="https://bugs.mojang.com/browse/MC-212687">MC-212687</a></p>
     *
     * <p>Removing {@link EntityLivingBase#getIdleTime()} check will restore the movement of squid if the player is far from them.</p>
     *
     * <p>FYI: I'm not sure what is a Mojang standard for mobs that are far from the player. Since Dolphins and Turtles don't freeze their movement when the player is far from them.</p>
     */
    @Mixin(targets = "net.minecraft.entity.passive.EntitySquid$AIMoveRandom")
    public abstract static class AIMoveRandom_MC212687 extends EntityAIBase
    {
        @Shadow
        @Final
        EntitySquid squid;

        @Override
        public void updateTask()
        {
            if (this.squid.getRNG().nextInt(50) == 0 || !this.squid.isInWater() || !this.squid.hasMovementVector())
            {
                float f = this.squid.getRNG().nextFloat() * (float) (Math.PI * 2);
                float tx = MathHelper.cos(f) * 0.2F;
                float ty = -0.1F + this.squid.getRNG().nextFloat() * 0.2F; // This constant will be replaced by `SquidRandomMovementGoal_MC132473`
                float tz = MathHelper.sin(f) * 0.2F;
                this.squid.setMovementVector(tx, ty, tz);
            }
        }
    }
}