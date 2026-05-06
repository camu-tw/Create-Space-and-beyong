package com.yourname.spaceengines.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ThermalShieldBlock extends Block {
    public static final MapCodec<ThermalShieldBlock> CODEC = simpleCodec(ThermalShieldBlock::new);
    public static final IntegerProperty HEAT_LEVEL = IntegerProperty.create("heat", 0, 4);

    public ThermalShieldBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(HEAT_LEVEL, 0));
    }

    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HEAT_LEVEL);
    }

    
    protected void randomTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int heat = state.getValue(HEAT_LEVEL);
        if (heat > 0) {
            level.setBlock(pos, state.setValue(HEAT_LEVEL, heat - 1), 3);
        }
    }
}