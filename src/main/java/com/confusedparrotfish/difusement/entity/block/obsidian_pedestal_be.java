package com.confusedparrotfish.difusement.entity.block;

import com.confusedparrotfish.difusement.util.SingleItemInventoryBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class obsidian_pedestal_be extends SingleItemInventoryBlockEntity {
    public obsidian_pedestal_be(BlockPos pos, BlockState state) {
        super(block_entities.OBSIDIAN_PEDESTAL_BET.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, obsidian_pedestal_be pEntity) {
        pEntity.tick_entity(level, pos, state);
        if (level.isClientSide()) {
            return;
        }
        // if (!pEntity.loaded) System.out.println(pEntity.loaded);
        // pEntity.load_tick();
    }
    
    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack item, Direction dir) {
        if (dir == Direction.DOWN)
            return EnchantmentHelper.getEnchantments(item).size() > 0;
        return !isEmpty();
    }
}
