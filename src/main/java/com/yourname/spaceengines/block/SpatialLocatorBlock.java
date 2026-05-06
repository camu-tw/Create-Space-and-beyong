package com.yourname.spaceengines.block;

import com.mojang.serialization.MapCodec;
import com.yourname.spaceengines.SpaceEnginesMod;
import com.yourname.spaceengines.block.entity.SpatialLocatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SpatialLocatorBlock extends BaseEntityBlock {
    public static final MapCodec<SpatialLocatorBlock> CODEC = simpleCodec(SpatialLocatorBlock::new);

    public SpatialLocatorBlock(Properties properties) {
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof SpatialLocatorBlockEntity locator)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (player.isShiftKeyDown()) {
                locator.setOrigin(pos);
                serverPlayer.sendSystemMessage(Component.literal("Spatial origin set: " + pos.toShortString()));
            } else {
                BlockPos origin = locator.getOrigin();
                serverPlayer.sendSystemMessage(Component.literal("Current XYZ: " + pos.toShortString() + " | Origin: " + origin.toShortString()));
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpatialLocatorBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, (BlockEntityType<SpatialLocatorBlockEntity>) SpaceEnginesMod.SPATIAL_LOCATOR_BLOCK_ENTITY.get(), SpatialLocatorBlockEntity::serverTick);
    }
}