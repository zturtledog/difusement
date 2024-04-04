package com.confusedparrotfish.difusement.entity.block;

import com.confusedparrotfish.difusement.util.SingleItemInventoryBlockEntity;
import com.confusedparrotfish.difusement.util.enchantingbook;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class difusement_altar_be extends SingleItemInventoryBlockEntity {
    public enchantingbook book = new enchantingbook();

    public difusement_altar_be(BlockPos pos, BlockState state) {
        super(block_entities.DIFUSEMENT_ALTAR_BET.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, difusement_altar_be pEntity) {
        if (level.isClientSide()) {
            pEntity.book.update(level, pos, state);
            return;
        }
        // System.out.println(pEntity.item);

        // level.setBlockEntity(pEntity);

        // todo: prossessing
    }
    
    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack item, Direction dir) {
        if (dir == Direction.DOWN)
            return EnchantmentHelper.getEnchantments(item).size() == 0;
        return !isEmpty();
    }
}
