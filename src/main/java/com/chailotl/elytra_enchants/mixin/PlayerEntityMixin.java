package com.chailotl.elytra_enchants.mixin;

import com.chailotl.elytra_enchants.ElytraEnchants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "startFallFlying",
            at = @At("HEAD"))
    private void elytraBoost(CallbackInfo info) {
        float strength = ElytraEnchants.getElytraLaunchStrength(this);

        if (strength > 0) {
            Vec3 vec = getLookAngle().scale(strength);
            push(vec.x, vec.y + 0.25F, vec.z);
        }
    }
}