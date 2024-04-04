package com.confusedparrotfish.difusement.item;

import com.confusedparrotfish.difusement.Difusement;
import com.google.common.base.Supplier;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class items {
    public static final DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, Difusement.MODID);

    public static RegistryObject<Item> item(String id, Supplier<Item> base) {
        return items.register(id, base);
    }

    public static void reg(IEventBus bus) {
        items.register(bus);
    }
}
