package com.chailotl.elytra_enchants;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

public class Trinkets {
    public static float getElytraLaunchStrength(LivingEntity entity) {
		Optional<ICuriosItemHandler> component = CuriosApi.getCuriosInventory(entity);
		if (component.isEmpty())
			return 0F;

		float strength = 0F;

        for (SlotResult slot : component.get().findCurios(Items.ELYTRA)) {
            MutableFloat mutableFloat = new MutableFloat();

            EnchantmentHelper.runIterationOnItem(slot.stack(), (enchantment, level) -> enchantment.value().modifyUnfilteredValue(ElytraEnchants.ELYTRA_LAUNCH_STRENGTH.get(), entity.getRandom(), level, mutableFloat));

            if (mutableFloat.floatValue() > strength) {
                strength = mutableFloat.floatValue();
            }
        }

        return strength;
    }

    public static boolean isElytraBounceOffFloor(LivingEntity entity) {
		Optional<ICuriosItemHandler> component = CuriosApi.getCuriosInventory(entity);
		if (component.isEmpty())
			return false;

		for (SlotResult slot : component.get().findCurios(Items.ELYTRA)) {
            if (EnchantmentHelper.has(slot.stack(), ElytraEnchants.ELYTRA_BOUNCE_OFF_FLOOR.get())) {
                return true;
            }
        }

        return false;
    }
}