package com.yrahcaz.sparkysessentialsmod.data.block;

import com.yrahcaz.sparkysessentialsmod.data.block.state.properties.LogicGateInputState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ExclusiveOrGateBlock extends LogicGateBlock {
    protected boolean determineIsPowered(Level level, BlockPos pos, BlockState state) {
        super.determineIsPowered(level, pos, state);

        LogicGateInputState inputState = level.getBlockState(pos).getValue(INPUT_STATE);
        if (inputState == LogicGateInputState.LEFT
        || inputState == LogicGateInputState.RIGHT)
            return true;

        return false;
    }
}
