package com.yrahcaz.sparkysessentialsmod.data.block;

import com.yrahcaz.sparkysessentialsmod.data.block.state.properties.LogicGateInputState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class OrGateBlock extends LogicGateBlock {
    protected boolean determineIsPowered(Level level, BlockPos pos, BlockState state) {
        super.determineIsPowered(level, pos, state);

        if (level.getBlockState(pos).getValue(INPUT_STATE) != LogicGateInputState.NONE)
            return true;

        return false;
    }
}
