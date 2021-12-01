package com.yrahcaz.sparkysessentialsmod.data.block;

import com.yrahcaz.sparkysessentialsmod.data.block.entity.LogicGateBlockEntity;
import com.yrahcaz.sparkysessentialsmod.data.block.state.properties.LogicGateInputState;
import com.yrahcaz.sparkysessentialsmod.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class AndGateBlock extends DiodeBlock implements EntityBlock {
//    public static final BooleanProperty POWERED_LEFT = BooleanProperty.create("powered_left");
//    public static final BooleanProperty POWERED_RIGHT = BooleanProperty.create("powered_right");
    public static final EnumProperty<LogicGateInputState> INPUT_STATE = EnumProperty.create("input_state", LogicGateInputState.class);

    public AndGateBlock(){
        super(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().sound(SoundType.WOOD));
        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(INPUT_STATE, LogicGateInputState.NONE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING, INPUT_STATE, POWERED);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return new LogicGateBlockEntity(pos, state);
    }

    protected int getDelay(BlockState state) { return 0; }

    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        if (determineIsPowered(level, pos, state))
            return true;

        return false;
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) { refreshOutputState(level, pos, state); }

    private void refreshOutputState(Level level, BlockPos pos, BlockState state) {
        boolean isPowered = determineIsPowered(level, pos, state);
        LogicGateBlockEntity blockEntity = getBlockEntity(level, pos);

        level.setBlock(pos, state.setValue(POWERED, isPowered), 2);
        blockEntity.setIsPowered(isPowered);
        updateNeighborsInFront(level, pos, state);
    }

    private boolean determineIsPowered(Level level, BlockPos pos, BlockState state) {
        refreshInputState(level, pos, state);
        return level.getBlockState(pos).getValue(INPUT_STATE) == LogicGateInputState.BOTH;
    }

    private void refreshInputState(Level level, BlockPos pos, BlockState state){
        Direction facing = level.getBlockState(pos).getValue(FACING);
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        boolean isPoweredLeft = getAlternateSignalAt(level, pos.relative(left), left) > 0;
        boolean isPoweredRight = getAlternateSignalAt(level, pos.relative(right), right) > 0;

        if (isPoweredLeft && isPoweredRight) { level.setBlock(pos, state.setValue(INPUT_STATE, LogicGateInputState.BOTH), 2); }
        else if (isPoweredLeft) { level.setBlock(pos, state.setValue(INPUT_STATE, LogicGateInputState.LEFT), 2); }
        else if (isPoweredRight) { level.setBlock(pos, state.setValue(INPUT_STATE, LogicGateInputState.RIGHT), 2); }
        else { level.setBlock(pos, state.setValue(INPUT_STATE, LogicGateInputState.NONE), 2); }
    }

    private LogicGateBlockEntity getBlockEntity(Level level, BlockPos pos) { return (LogicGateBlockEntity)level.getBlockEntity(pos); }
}
