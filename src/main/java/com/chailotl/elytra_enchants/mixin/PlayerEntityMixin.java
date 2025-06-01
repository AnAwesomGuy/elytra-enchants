package com.chailotl.elytra_enchants.mixin;

import com.chailotl.elytra_enchants.ElytraEnchants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "startFallFlying",
            at = @At("HEAD"))
    private void elytraBoost(CallbackInfo info) {
        float strength = ElytraEnchants.getElytraLaunchStrength(this);

        if (strength > 0) {
            Vec3d vec = getRotationVector().multiply(strength);
            addVelocity(vec.x, vec.y + 0.25F, vec.z);
        }
    }
}