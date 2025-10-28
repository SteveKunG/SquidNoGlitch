package com.stevekung.squidnoglitch.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.util.MathHelper;

@Mixin(EntitySquid.class)
public class MixinEntitySquid extends EntityWaterMob
{
    @Shadow
    float randomMotionVecX;

    @Shadow
    float randomMotionVecY;

    @Shadow
    float randomMotionVecZ;

    MixinEntitySquid()
    {
        super(null);
    }

    /**
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-39263">MC-39263</a>, <a href="https://bugs.mojang.com/browse/MC-58294">MC-58294</a>, <a href="https://bugs.mojang.com/browse/MC-89883">MC-89883</a>, <a href="https://bugs.mojang.com/browse/MC-136421">MC-136421</a>, <a href="https://bugs.mojang.com/browse/MC-212213">MC-212213</a>, <a href="https://bugs.mojang.com/browse/MC-225422">MC-225422</a>
     * and partially fix <a href="https://bugs.mojang.com/browse/MC-134626">MC-134626</a></p>
     *
     * <p>Add {@link EntityLiving#isClientWorld()} check to prevent movement de-sync on the client.</p>
     */
    @Override
    public void moveEntityWithHeading(float strafe, float forward)
    {
        // It is server world, good old mapping name
        if (this.isClientWorld())
        {
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
        }
    }

    /**
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-132473">MC-132473</a></p>
     *
     * <p>Decreasing Y movement vector to {@code 0.15f} which should stop squids become stuck on land.</p>
     */
    @ModifyConstant(method = "updateEntityActionState", remap = false, constant = @Constant(floatValue = 0.2F, ordinal = 1))
    private float squidnoglitch$modifyYMovementSpeed(float oldSpeed)
    {
        return 0.15F;
    }

    /**
     * <p>Fix <a href="https://bugs.mojang.com/browse/MC-212687">MC-212687</a></p>
     *
     * <p>Removing {@code this.entityAge > 100} check will restore the movement of squid if the player is far from them.</p>
     *
     * <p>FYI: I'm not sure what is a Mojang standard for mobs that are far from the player. Since Dolphins and Turtles don't freeze their movement when the player is far from them.</p>
     */
    @Override
    public void updateEntityActionState()
    {
        ++this.entityAge;

        if (this.rand.nextInt(50) == 0 || !this.inWater || this.randomMotionVecX == 0.0F && this.randomMotionVecY == 0.0F && this.randomMotionVecZ == 0.0F)
        {
            float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
            this.randomMotionVecX = MathHelper.cos(f) * 0.2F;
            this.randomMotionVecY = -0.1F + this.rand.nextFloat() * 0.2F; // This constant will be replaced by `squidnoglitch$modifyYMovementSpeed`
            this.randomMotionVecZ = MathHelper.sin(f) * 0.2F;
        }
        this.despawnEntity();
    }
}