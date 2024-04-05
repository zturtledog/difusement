package com.confusedparrotfish.difusement.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

import com.confusedparrotfish.difusement.entity.block.block_entities;
import com.confusedparrotfish.difusement.entity.block.obsidian_pedestal_be;

public class obsidian_pedestal extends BaseEntityBlock {
    protected obsidian_pedestal(Properties p_49224_) {
        super(p_49224_);
    }

    public static final VoxelShape shape = Stream.of(
            Block.box(0.75, 2, 0.75, 15.25, 14, 15.25),
            Block.box(0, 14, 0, 16, 16.000000000000007, 16),
            Block.box(0, -7.105427357601002e-15, 0, 16, 2, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR))
            .get();

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new obsidian_pedestal_be(pos,state);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_,
            CollisionContext p_60558_) {
        return shape;
    }

    @Override
    public void onRemove(BlockState pstate, Level level, BlockPos pos, BlockState nstate,
            boolean moving) {
        if (pstate.getBlock() != nstate.getBlock()) {
            BlockEntity ent = level.getBlockEntity(pos);
            if (ent instanceof obsidian_pedestal_be) {
                ((obsidian_pedestal_be) ent).drops();
            }
        }
        
        level.removeBlockEntity(pos);
    }

    @Override
    public InteractionResult use(BlockState state, Level lvl, BlockPos pos, Player plr,
            InteractionHand hand, BlockHitResult hit) {

        ItemStack itm = plr.getItemInHand(hand).copy();
        obsidian_pedestal_be be = (obsidian_pedestal_be)lvl.getBlockEntity(pos); 
        if (!be.isEmpty() && itm.isEmpty()) {
            plr.setItemInHand(hand, be.item);
            be.setItem(ItemStack.EMPTY);
        } else if (be.isEmpty() && !itm.isEmpty()) {
            be.setItem(itm);
            plr.setItemInHand(hand, ItemStack.EMPTY);
        } else if (!be.isEmpty() && !itm.isEmpty()) {
            plr.setItemInHand(hand, be.item);
            be.setItem(itm);
        }
        
        return InteractionResult.CONSUME_PARTIAL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, block_entities.OBSIDIAN_PEDESTAL_BET.get(), obsidian_pedestal_be::tick);
    }
}
