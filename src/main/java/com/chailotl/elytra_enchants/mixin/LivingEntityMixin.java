package com.chailotl.elytra_enchants.mixin;

import com.chailotl.elytra_enchants.ElytraEnchants;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique
	private int elytra_enchants$ticksOnGround = 0;

	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@WrapOperation(
			method = "travel",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;setSharedFlag(IZ)V"
			))
	public void cancelElytraCancel(LivingEntity entity, int index, boolean value, Operation<Void> original) {
		if (!ElytraEnchants.isElytraBounceOffFloor(entity))
			original.call(entity, index, value);
	}

	@WrapOperation(
			method = "updateFallFlying",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;setSharedFlag(IZ)V"
			))
	public void initAi(LivingEntity entity, int index, boolean value, Operation<Void> original) {
		if (ElytraEnchants.isElytraBounceOffFloor(entity) && (entity.getDeltaMovement().y != 0 || ++elytra_enchants$ticksOnGround < 4))
			return;
		original.call(entity, index, value);
	}

	@Inject(
			method = "tick",
			at = @At("TAIL")
	)
	public void resetTicksOnGround(CallbackInfo ci) {
		if (getDeltaMovement().y != 0)
			elytra_enchants$ticksOnGround = 0;
	}
}
