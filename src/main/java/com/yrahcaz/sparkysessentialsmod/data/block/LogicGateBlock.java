package com.yrahcaz.sparkysessentialsmod.data.block;

import com.yrahcaz.sparkysessentialsmod.data.block.state.properties.LogicGateInputState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class LogicGateBlock extends DiodeBlock {
    public static final EnumProperty<LogicGateInputState> INPUT_STATE = EnumProperty.create("input_state", LogicGateInputState.class);

    public LogicGateBlock(){
        super(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().sound(SoundType.WOOD));
        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(INPUT_STATE, LogicGateInputState.NONE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING, INPUT_STATE, POWERED);
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

        level.setBlock(pos, state.setValue(POWERED, isPowered), 2);
        updateNeighborsInFront(level, pos, state);
    }

    protected boolean determineIsPowered(Level level, BlockPos pos, BlockState state) {
        refreshInputState(level, pos, state);
        return false;
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
}
