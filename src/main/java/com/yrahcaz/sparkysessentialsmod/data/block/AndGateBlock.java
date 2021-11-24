package com.yrahcaz.sparkysessentialsmod.data.block;

import com.yrahcaz.sparkysessentialsmod.data.block.entity.LogicGateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.TickPriority;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class AndGateBlock extends DiodeBlock implements EntityBlock {
    public AndGateBlock(){
        super(BlockBehaviour.Properties.of(Material.DECORATION));
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
    }

    protected int getDelay(BlockState state) { return 0; }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) { stateBuilder.add(FACING, POWERED); }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return new LogicGateBlockEntity(pos, state);
    }

    protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
        if (!level.getBlockTicks().willTickThisTick(pos, this)) {
            if (determineIsPowered(level, pos, state) != ((LogicGateBlockEntity)level.getBlockEntity(pos)).getIsPowered()
                    || state.getValue(POWERED) != shouldTurnOn(level, pos, state)) {
                TickPriority tickpriority = shouldPrioritize(level, pos, state) ? TickPriority.HIGH : TickPriority.NORMAL;
                level.getBlockTicks().scheduleTick(pos, this, 2, tickpriority);
            }
        }
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) { refreshOutputState(level, pos, state); }

    private void refreshOutputState(Level level, BlockPos pos, BlockState state) {
        boolean isPowered = determineIsPowered(level, pos, state);
        LogicGateBlockEntity blockEntity = (LogicGateBlockEntity)level.getBlockEntity(pos);

        if(isPowered != blockEntity.getIsPowered()) {
            level.setBlock(pos, state.setValue(POWERED, isPowered), 2);
            updateNeighborsInFront(level, pos, state);
            blockEntity.setIsPowered(isPowered);
        }
    }

    private boolean determineIsPowered(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        int leftSignal = getAlternateSignalAt(level, pos.relative(left), left);
        int rightSignal = getAlternateSignalAt(level, pos.relative(right), right);

        return leftSignal > 0 && rightSignal > 0;
//        return state.getValue(FACING) == Direction.NORTH;
    }
}
