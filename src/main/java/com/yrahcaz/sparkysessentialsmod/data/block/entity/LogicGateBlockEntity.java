package com.yrahcaz.sparkysessentialsmod.data.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LogicGateBlockEntity extends BlockEntity {
    private boolean isPowered;

    public LogicGateBlockEntity(BlockPos pos, BlockState state){
        super(BlockEntityType.COMPARATOR, pos, state);
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("IsPowered", isPowered);
        return tag;
    }

    public void load(CompoundTag tag) {
        isPowered = tag.getBoolean("IsPowered");
    }

    public boolean getIsPowered() { return this.isPowered; }
    public void setIsPowered(boolean isPowered) { this.isPowered = isPowered; }
}
