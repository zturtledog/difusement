package com.confusedparrotfish.difusement.entity.block;

import com.confusedparrotfish.difusement.Difusement;
import com.confusedparrotfish.difusement.block.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class block_entities {
    public static final DeferredRegister<BlockEntityType<?>> entities =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Difusement.MODID);

    public static final RegistryObject<BlockEntityType<difusement_altar_be>> DIFUSEMENT_ALTAR_BET = entity("difusement_altar_be", difusement_altar_be::new, blocks.DIFUSEMENT_ALTAR);
    public static final RegistryObject<BlockEntityType<obsidian_pedestal_be>> OBSIDIAN_PEDESTAL_BET = entity("obsidian_pedestal_be", obsidian_pedestal_be::new, blocks.OBSIDIAN_PEDESTAL);

    public static <B extends BlockEntity> RegistryObject<BlockEntityType<B>> entity(String id, BlockEntityType.BlockEntitySupplier<B> create, RegistryObject<Block> block) {
        return entities.register(id, () ->
                BlockEntityType.Builder.of(create,
                        block.get()).build(null));
    }
}
