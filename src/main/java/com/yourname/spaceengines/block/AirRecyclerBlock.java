package com.yourname.spaceengines.block;

import com.mojang.serialization.MapCodec;
import com.yourname.spaceengines.SpaceEnginesMod;
import com.yourname.spaceengines.block.entity.AirRecyclerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AirRecyclerBlock extends BaseEntityBlock {
    public static final MapCodec<AirRecyclerBlock> CODEC = simpleCodec(AirRecyclerBlock::new);

    public AirRecyclerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AirRecyclerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, (BlockEntityType<AirRecyclerBlockEntity>) SpaceEnginesMod.AIR_RECYCLER_BLOCK_ENTITY.get(), AirRecyclerBlockEntity::serverTick);
    }
}