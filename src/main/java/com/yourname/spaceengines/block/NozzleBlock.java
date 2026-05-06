package com.yourname.spaceengines.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class NozzleBlock extends Block {
    public static final MapCodec<NozzleBlock> CODEC = simpleCodec(NozzleBlock::new);
    public static final BooleanProperty HOT = BooleanProperty.create("hot");

    public NozzleBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(HOT, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HOT);
    }

    public static int calculateNozzleGroupSize(Level level, BlockPos origin) {
        int neighbors = 1;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(origin.relative(direction)).getBlock() instanceof NozzleBlock) {
                neighbors++;
            }
        }
        return Math.min(neighbors, 9);
    }
}