package com.chailotl.elytra_enchants;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.curios.api.CuriosApi;

@Mod(ElytraEnchants.MOD_ID)
public class ElytraEnchants {
    public static final String MOD_ID = "elytra_enchants";

    public static final boolean CURIOS_LOADED = ModList.get().isLoaded(CuriosApi.MODID);

    private static final DeferredRegister<DataComponentType<?>> COMPONENT_REGISTRAR = DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> ELYTRA_BOUNCE_OFF_FLOOR =
            COMPONENT_REGISTRAR.register("elytra_bounce_off_floor", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnchantmentValueEffect>> ELYTRA_LAUNCH_STRENGTH =
            COMPONENT_REGISTRAR.register("elytra_launch_strength", () -> DataComponentType.<EnchantmentValueEffect>builder().persistent(EnchantmentValueEffect.CODEC).build());

    public ElytraEnchants(IEventBus bus) {
        COMPONENT_REGISTRAR.register(bus);
    }

    public static float getElytraLaunchStrength(LivingEntity entity) {
        MutableFloat mutableFloat = new MutableFloat();

        // check chest slot
        EnchantmentHelper.runIterationOnItem(entity.getItemBySlot(EquipmentSlot.CHEST), (enchantment, level) -> enchantment.value().modifyUnfilteredValue(ELYTRA_LAUNCH_STRENGTH.get(), entity.getRandom(), level, mutableFloat));

        // Curios compat
        if (CURIOS_LOADED) {
            float strength = Trinkets.getElytraLaunchStrength(entity);

            if (strength > mutableFloat.floatValue()) {
                return strength;
            }
        }

        return mutableFloat.floatValue();
    }

    public static boolean isElytraBounceOffFloor(LivingEntity entity) {
        return EnchantmentHelper.has(entity.getItemBySlot(EquipmentSlot.CHEST), ElytraEnchants.ELYTRA_BOUNCE_OFF_FLOOR.get()) || CURIOS_LOADED && Trinkets.isElytraBounceOffFloor(entity);
    }
}