package com.confusedparrotfish.difusement.block;

import com.confusedparrotfish.difusement.Difusement;
import com.confusedparrotfish.difusement.item.items;
import com.google.common.base.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class blocks {
    public static final DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, Difusement.MODID);

    public static final RegistryObject<Block> DIFUSEMENT_ALTAR = block_item("difusement_altar", ()->new difusement_altar(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.GOLD).lightLevel((state) -> {
        return 7;
    })));
    public static final RegistryObject<Block> OBSIDIAN_PEDESTAL = block_item("obsidian_pedestal", ()->new obsidian_pedestal(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops()));

    public static RegistryObject<Block> block(String id, Supplier<Block> base) {
        return blocks.register(id, base);
    }

    public static RegistryObject<Block> block_item(String id, Supplier<Block> base, Item.Properties props) {
        RegistryObject<Block> ret = blocks.register(id, base);
        items.item(id, () -> new BlockItem(ret.get(), props));
        return ret;
    }

    public static RegistryObject<Block> block_item(String id, Supplier<Block> base) {
        RegistryObject<Block> ret = blocks.register(id, base);
        items.item(id, () -> new BlockItem(ret.get(), new Item.Properties()));
        return ret;
    }
}
