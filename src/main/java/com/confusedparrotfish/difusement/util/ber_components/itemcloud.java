package com.confusedparrotfish.difusement.util.ber_components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import com.confusedparrotfish.difusement.util.util.doing;
import com.confusedparrotfish.difusement.util.animationHelper;
import java.util.ArrayList;

public class itemcloud {
    ArrayList<item_instance> instances = new ArrayList<>();
    ItemStack item = ItemStack.EMPTY;
    public int max = 0;

    public itemcloud(int max, ItemStack item) {
        this.max = max;
        this.item = item;
    }

    public void update(Level lvl, BlockPos pos, BlockState state) {
        // if (instances.isEmpty()) {
        //     instances.add(new item_instance(Vec3.ZERO,new Vec3(random_float(),random_float(),random_float()),0.02f));
        //     instances.add(new item_instance(Vec3.ZERO,new Vec3(random_float(),random_float(),random_float()),0.02f));
        //     instances.add(new item_instance(Vec3.ZERO,new Vec3(random_float(),random_float(),random_float()),0.02f));
        //     instances.add(new item_instance(Vec3.ZERO,new Vec3(random_float(),random_float(),random_float()),0.02f));
        //     instances.add(new item_instance(Vec3.ZERO,new Vec3(random_float(),random_float(),random_float()),0.02f));
        //     instances.add(new item_instance(Vec3.ZERO,new Vec3(random_float(),random_float(),random_float()),0.02f));
        //     instances.add(new item_instance(Vec3.ZERO,new Vec3(random_float(),random_float(),random_float()),0.02f));
        //     instances.add(new item_instance(Vec3.ZERO,new Vec3(random_float(),random_float(),random_float()),0.02f));
        // }
        boolean hasremoved = false;
        for (item_instance instance : instances) {
            if (instance.next == doing.NOTHING) {
                instance.target = new Vec3(0,-1.5,0);
            }
            instance.pos = (instance.pos.lerp(instance.target,instance.speed));
            if (instance.pos.distanceToSqr(instance.target) < 0.01) {
                if (instance.next == doing.SOMETHING) {
                    instance.target = new Vec3(random_float(),random_float(),random_float());
                }
            }
        }
        if (hasremoved) {
            ArrayList<item_instance> temp = new ArrayList<>();
            for (item_instance instance : instances) {
                if (instance != null) {
                    temp.add(instance);
                }
            }
            instances = temp;
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("length", instances.size());
        System.out.println(instances.size());
        return tag;
    }

    public void load(CompoundTag tag) {
        instances = new ArrayList<>();
        for (int i = 0; i < tag.getInt("length"); i++) {
            instances.add(new item_instance(Vec3.ZERO,new Vec3(
                random_float(),
                random_float(),
                random_float()
            ), 0.02f));
        }
        System.out.println(instances.size());
    }

    public void render(float delta, PoseStack pose, MultiBufferSource bufffer, int light, int overlay, ItemRenderer itemRenderer, animationHelper entity, BlockPos pos) {
        pose.pushPose();
        float bob = (ease(Math.abs((entity.offtime() % 50) / 50.0f - 0.5f) * 2f) - 0.5f) / 6f;
        for (item_instance instance : instances) {
            pose.pushPose();
            pose.translate(instance.pos.x+0.5,instance.pos.y+1.5f,instance.pos.z+0.5);
            pose.scale(0.3f, 0.3f, 0.3f);
            pose.mulPose(Quaternion.fromXYZDegrees(new Vector3f(bob / 10f, entity.offtime(), bob / 10f)));
            itemRenderer.renderStatic(item, ItemTransforms.TransformType.FIXED, light, overlay, pose, bufffer,
                    (int) pos.asLong());
                
            pose.popPose();
        }
        pose.popPose();
    }

    public float ease(float n) {
        return 1f - (float) (13.6 * Math.pow(n, 2) * Math.pow(n - 1, 2));
    }

    public static float random_float() {
        return (float)(Math.random()-0.5)*2f;
    }

    public static class item_instance {
        public Vec3 pos = Vec3.ZERO;
        public Vec3 target = Vec3.ZERO;
        public float speed = 0f;
        public doing next = doing.SOMETHING;
        
        public item_instance(Vec3 pos, Vec3 target, float speed) {
            this.pos = pos;
            this.target = target;
            this.speed = speed;
        }

        public static item_instance rand(Vec3 pos) {
            return new item_instance(pos,new Vec3(random_float(),random_float(),random_float()),0.02f);
        }
    }

    public void set_count(int count) {
        // if (instances.size() > count) {
        //     for (int i = 0; i < instances.size()-count; i++) {
        //         instances.remove(0);
        //     }
        // } else if (instances.size() < count) {
        //     for (int i = 0; i < count-instances.size(); i++) {
        //         instances.add(item_instance.rand(Vec3.ZERO));
        //     }
        // }
        while (count() > count) {
            instances.remove(0);
        }
        while (count() < count) {
            instances.add(item_instance.rand(Vec3.ZERO));
        }
    }

    public void set_task(int index, doing action) {
        if (index < instances.size()) {
            instances.get(index).next = action;
        }
    }

    public int count() {
        return instances.size();
    }

    public doing task(int index) {
        if (index < instances.size()) {
            return instances.get(index).next;
        }
        return doing.NOTHING;
    } 

    public boolean isfull() {
        return !(instances.size() < max);
    }

    public void add(item_instance inst) {
        instances.add(inst);
    }

    public static itemcloud readpacket(FriendlyByteBuf buf) {
        int count = buf.readInt();
        ItemStack item = buf.readItem();
        int max = buf.readInt();
        byte[] doings = buf.readByteArray();

        itemcloud ret = new itemcloud(max, item);
        ret.set_count(count);
        for (int i = 0; i < doings.length; i++) {
            ret.set_task(i,doing.dbite(doings[i]));
        }
        return ret;
    }

    public void writepacket(FriendlyByteBuf buf) {
        buf.writeInt(count());
        buf.writeItem(item);
        buf.writeInt(max);
        byte[] doings = new byte[count()]; 
        for (int i = 0; i < count(); i++) {
            doings[i] = instances.get(i).next.bite();
        }
        buf.writeByteArray(doings);
    }
}
