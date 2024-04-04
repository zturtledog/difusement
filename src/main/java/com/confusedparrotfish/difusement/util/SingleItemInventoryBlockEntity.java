package com.confusedparrotfish.difusement.util;

import com.confusedparrotfish.difusement.network.message;
import com.confusedparrotfish.difusement.network.packet.ItemStackSyncS2CPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SingleItemInventoryBlockEntity extends BlockEntity implements WorldlyContainer {
    public SingleItemInventoryBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    public ItemStack item = ItemStack.EMPTY;

    public void drops() {
        Containers.dropContents(level, worldPosition, this);
    }

    public void notifyClient() {
        setChanged();
        if (!level.isClientSide()) {
            message.sendToClients(new ItemStackSyncS2CPacket(this.item, worldPosition));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("item", item.save(new CompoundTag()));
        System.out.println(item);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        item = ItemStack.of(tag.getCompound("item"));
        System.out.println(item);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return item.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return item;
    }

    @Override
    public ItemStack removeItem(int slot, int ammount) {
        ItemStack itm = item.copy();
        if (ammount > item.getCount())
            ammount = item.getCount();
        itm.setCount(ammount);
        item.shrink(ammount);
        notifyClient();
        return itm;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return removeItem(slot, 1);
    }

    @Override
    public void setItem(int slot, ItemStack itm) {
        if (isEmpty()) {
            item = itm;
            notifyClient();
        }
    }

    public void setItem(ItemStack itm) {
        item = itm;
        notifyClient();
    }

    // blindly coppied from wherever
    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(player.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
                    (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clearContent() {
        item = ItemStack.EMPTY;
        notifyClient();
    }

    @Override
    public int[] getSlotsForFace(Direction dir) {
        return new int[] { 0 };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack item, Direction dir) {
        return !isEmpty();
    }
}
