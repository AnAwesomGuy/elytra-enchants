package com.chailotl.elytra_enchants.mixin;

import com.chailotl.elytra_enchants.ElytraEnchants;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique
	private int ticksOnGround = 0;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@WrapOperation(
			method = "travel",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"
			))
	public void cancelElytraCancel(LivingEntity entity, int index, boolean value, Operation<Void> original) {
		if (!ElytraEnchants.isElytraBounceOffFloor(entity))
			original.call(entity, index, value);
	}

	@WrapOperation(
			method = "tickFallFlying",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"
			))
	public void initAi(LivingEntity entity, int index, boolean value, Operation<Void> original) {
		if (ElytraEnchants.isElytraBounceOffFloor(entity) && (entity.getVelocity().y != 0 || ++ticksOnGround < 4))
			return;
		original.call(entity, index, value);
	}

	@Inject(
			method = "tick",
			at = @At("TAIL")
	)
	public void resetTicksOnGround(CallbackInfo ci) {
		if (getVelocity().y != 0)
			ticksOnGround = 0;
	}
}
