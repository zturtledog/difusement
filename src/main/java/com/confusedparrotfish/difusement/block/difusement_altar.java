package com.confusedparrotfish.difusement.block;

import com.confusedparrotfish.difusement.entity.block.block_entities;
import com.confusedparrotfish.difusement.entity.block.difusement_altar_be;

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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class difusement_altar extends BaseEntityBlock {
    protected difusement_altar(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new difusement_altar_be(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, block_entities.DIFUSEMENT_ALTAR_BET.get(), difusement_altar_be::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level lvl, BlockPos pos, Player plr,
            InteractionHand hand, BlockHitResult hit) {

        ItemStack itm = plr.getItemInHand(hand).copy();
        difusement_altar_be be = (difusement_altar_be)lvl.getBlockEntity(pos); 
        if (!be.isEmpty() && itm.isEmpty()) {
            plr.setItemInHand(hand, be.item);
            be.setItem(ItemStack.EMPTY);
            lvl.setBlockEntity(be);
        } else if (be.isEmpty() && !itm.isEmpty()) {
            be.setItem(itm);
            plr.setItemInHand(hand, ItemStack.EMPTY);
            be.setChanged();
            lvl.setBlockEntity(be);
        } else if (!be.isEmpty() && !itm.isEmpty()) {
            plr.setItemInHand(hand, be.item);
            be.setItem(itm);
            be.setChanged();
            lvl.setBlockEntity(be);
        }
        
        return InteractionResult.CONSUME_PARTIAL;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    static final VoxelShape shape = Block.box(0, 0.0001, 0, 16, 11.25, 16);
    //Stream.of(Block.box(0, 1, 0, 16, 8, 16),Block.box(0.25, 0, 0.25, 15.75, 1, 15.75)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

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
            if (ent instanceof difusement_altar_be) {
                ((difusement_altar_be) ent).drops();
            }
        }
        
        level.removeBlockEntity(pos);
    }
}
