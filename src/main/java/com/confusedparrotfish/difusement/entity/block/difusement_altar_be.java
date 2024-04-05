package com.confusedparrotfish.difusement.entity.block;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;

import com.confusedparrotfish.difusement.block.blocks;
import com.confusedparrotfish.difusement.network.message;
import com.confusedparrotfish.difusement.network.packet.ItemCloudSyncS2CPacket.cloudImplementor;
import com.confusedparrotfish.difusement.network.packet.DifusementAltarSyncS2CPacket.difusement_altar_packet;
import com.confusedparrotfish.difusement.network.packet.ItemCloudSyncS2CPacket;
import com.confusedparrotfish.difusement.util.SingleItemInventoryBlockEntity;
import com.confusedparrotfish.difusement.util.ber_components.enchantingbook;
import com.confusedparrotfish.difusement.util.ber_components.itemcloud;
import com.confusedparrotfish.difusement.util.ber_components.itemcloud.item_instance;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class difusement_altar_be extends SingleItemInventoryBlockEntity implements cloudImplementor {
    public enchantingbook book = new enchantingbook();
    public itemcloud cloud = new itemcloud(8, Items.LAPIS_LAZULI.getDefaultInstance());
    public int[] progress = new int[8];

    public static final int CONVERSION_TIME = 20 * 4;

    public difusement_altar_be(BlockPos pos, BlockState state) {
        super(block_entities.DIFUSEMENT_ALTAR_BET.get(), pos, state);
    }

    @Override
    public void notifyClient() {
        super.notifyClient();
        if (!level.isClientSide()) {
            message.sendToClients(new ItemCloudSyncS2CPacket(cloud, worldPosition));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("lapi", cloud.save());
        return tag;
    }

    @Override
    public void drops() {
        super.drops();
        ItemStack lapis = Items.LAPIS_LAZULI.getDefaultInstance();
        lapis.setCount(cloud.count());
        Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), lapis);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(worldPosition))
                .expandTowards(1, 2, 1)
                .expandTowards(1, 0, -1);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, difusement_altar_be ent) {
        ent.tick_entity(level, pos, state);
        if (level.isClientSide()) {
            ent.cloud.update(level, pos, state);
            ent.book.update(level, pos, state);
            return;
        }

        if (!ent.cloud.isfull()) {
            BlockPos pos_above = pos.above();
            List<ItemEntity> items = level.getEntitiesOfClass(
                    ItemEntity.class,
                    AABB.unitCubeFromLowerCorner(new Vec3(pos_above.getX(), pos_above.getY(), pos_above.getZ()))
                            .move(new Vec3(0, -0.5, 0)),
                    EntitySelector.ENTITY_STILL_ALIVE);

            ItemEntity lapi = null;
            int largest_lapi = 0;
            for (ItemEntity item : items) {
                if (item.getItem().is(Items.LAPIS_LAZULI)) {
                    if (item.getItem().getCount() > largest_lapi) {
                        largest_lapi = item.getItem().getCount();
                        lapi = item;
                    }
                }
            }
            if (lapi != null) {
                ent.cloud.add(item_instance.rand(lapi.position().subtract(Vec3.atLowerCornerOf(pos))));
                lapi.getItem().shrink(1);
                ent.notifyClient();
            }
        }

        ArrayList<Integer> spreadpoints = new ArrayList<>();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(ent.item);
        if (enchantments.size() > 0) {
            // System.out.println("am i a book?");
            List<Integer> valids = get_structure(level, pos, state, ent);
            for (int i = 0; i < ent.progress.length; i++) {
                if (valids.contains(i)) {
                    ent.progress[i]++;
                    if (ent.progress[i] > CONVERSION_TIME) {
                        spreadpoints.add(i);
                        // EnchantmentHelper.setEnchantments(enchantments, be.item);
                        // be.notifyClient();
                    }
                } else {
                    ent.progress[i] = 0;
                }
            }
            if (spreadpoints.size() > 0) {
                int i = 0;
                int consumed = 0;
                HashMap<Enchantment, Integer> post_enchantments = new HashMap<Enchantment, Integer>();
                for (Entry<Enchantment, Integer> ench : enchantments.entrySet()) {
                    if (i < spreadpoints.size() && consumed < ent.cloud.count()) {
                        // set spreadpoint
                        obsidian_pedestal_be be = (obsidian_pedestal_be) level
                                .getBlockEntity(pos.offset(struct_offsets[valids.get(i)]));
                        ItemStack itm = new ItemStack(Items.ENCHANTED_BOOK);
                        EnchantedBookItem.addEnchantment(itm, new EnchantmentInstance(ench.getKey(), ench.getValue()));
                        if (be.item.getCount() > 1) {
                            be.item.shrink(1);
                            Containers.dropItemStack(level,
                                    be.getBlockPos().getX() + 0.5,
                                    be.getBlockPos().getY() + 1.5,
                                    be.getBlockPos().getZ() + 0.5, itm);
                            be.notifyClient();
                        } else {
                            be.setItem(itm);
                        }
                        ent.progress[valids.get(i)] = 0;
                        consumed++;
                    } else {
                        post_enchantments.put(ench.getKey(), ench.getValue());
                    }
                    i++;
                }
                if (consumed > 0) {
                    ent.cloud.set_count(ent.cloud.count() - consumed);
                    if (ent.item.is(Items.ENCHANTED_BOOK)) { // stacks to one ; should still have safeguards but it's
                                                             // whatever for now
                        if (post_enchantments.size() > 0) {
                            ent.item = new ItemStack(Items.ENCHANTED_BOOK);

                            for (Entry<Enchantment, Integer> ench : post_enchantments.entrySet()) {
                                EnchantedBookItem.addEnchantment(ent.item,
                                        new EnchantmentInstance(ench.getKey(), ench.getValue()));
                            }
                        } else {
                            ent.item = new ItemStack(Items.BOOK);
                        }
                    } else {
                        if (ent.item.getCount() > 1) {
                            ItemStack world = ent.item.copy();
                            ent.item.shrink(1);
                            world.setCount(1);
                            EnchantmentHelper.setEnchantments(post_enchantments, world);
                            Containers.dropItemStack(level,
                                    ent.getBlockPos().getX() + 0.5,
                                    ent.getBlockPos().getY() + 1.5,
                                    ent.getBlockPos().getZ() + 0.5, world);
                        } else {
                            EnchantmentHelper.setEnchantments(post_enchantments, ent.item);
                        }
                    }
                    ent.notifyClient();
                }
            }
        } else {
            for (int i = 0; i < ent.progress.length; i++) {
                ent.progress[i] = 0;
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("lapi", cloud.save());
        System.out.println(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        cloud.load(tag.getCompound("lapi"));
    }

    public static final BlockPos[] struct_offsets = new BlockPos[] {
            new BlockPos(3, 0, 0),
            new BlockPos(-3, 0, 0),
            new BlockPos(0, 0, 3),
            new BlockPos(0, 0, -3),

            new BlockPos(2, 0, 2),
            new BlockPos(2, 0, -2),
            new BlockPos(-2, 0, 2),
            new BlockPos(-2, 0, -2),
    };

    public static List<Integer> get_structure(Level level, BlockPos pos, BlockState state, difusement_altar_be ent) {
        ArrayList<Integer> valids = new ArrayList<>();

        for (int i = 0; i < struct_offsets.length; i++) {
            // System.out.println(level.getBlockState(pos.offset(struct_offsets[i])));
            if (level.getBlockState(pos.offset(struct_offsets[i])).is(blocks.OBSIDIAN_PEDESTAL.get())) {
                BlockEntity entity = level.getBlockEntity(pos.offset(struct_offsets[i]));
                if (entity != null && entity instanceof obsidian_pedestal_be &&
                        ((obsidian_pedestal_be) entity).item.is(Items.BOOK) &&
                        !((obsidian_pedestal_be) entity).item.isEnchanted()) {
                    valids.add(i);
                }
            }
        }

        return valids;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack item, Direction dir) {
        if (dir == Direction.DOWN)
            return EnchantmentHelper.getEnchantments(item).size() == 0;
        return !isEmpty();
    }

    @Override
    public void cloud_update(itemcloud cloud) {
        this.cloud.set_count(cloud.count());
        for (int i = 0; i < cloud.count(); i++) {
            this.cloud.set_task(i, cloud.task(i));
        }
    }

    public void recv_packet(difusement_altar_packet pack) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recv_packet'");
    }
}
