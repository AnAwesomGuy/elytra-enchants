package com.chailotl.elytra_enchants;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.Optional;

public class Trinkets {
    public static float getElytraLaunchStrength(LivingEntity entity) {
		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(entity);
		if (component.isEmpty())
			return 0F;

		float strength = 0F;

        for (Pair<SlotReference, ItemStack> pair : component.get().getEquipped(Items.ELYTRA)) {
            MutableFloat mutableFloat = new MutableFloat();

            EnchantmentHelper.forEachEnchantment(pair.getRight(), (enchantment, level) -> enchantment.value().modifyValue(ElytraEnchants.ELYTRA_LAUNCH_STRENGTH, entity.getRandom(), level, mutableFloat));

            if (mutableFloat.floatValue() > strength) {
                strength = mutableFloat.floatValue();
            }
        }

        return strength;
    }

    public static boolean isElytraBounceOffFloor(LivingEntity entity) {
		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(entity);
		if (component.isEmpty())
			return false;

        for (Pair<SlotReference, ItemStack> pair : component.get().getEquipped(Items.ELYTRA)) {
            if (EnchantmentHelper.hasAnyEnchantmentsWith(pair.getRight(), ElytraEnchants.ELYTRA_BOUNCE_OFF_FLOOR)) {
                return true;
            }
        }

        return false;
    }
}