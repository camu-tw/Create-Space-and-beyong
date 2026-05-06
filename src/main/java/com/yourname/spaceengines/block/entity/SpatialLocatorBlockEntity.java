package com.yourname.spaceengines.block.entity;

import com.yourname.spaceengines.SpaceEnginesMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SpatialLocatorBlockEntity extends BlockEntity {
    private BlockPos origin = BlockPos.ZERO;

    public SpatialLocatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(SpaceEnginesMod.SPATIAL_LOCATOR_BLOCK_ENTITY.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SpatialLocatorBlockEntity blockEntity) {
        if (blockEntity.origin.equals(BlockPos.ZERO)) {
            blockEntity.origin = pos;
            blockEntity.setChanged();
        }
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public void setOrigin(BlockPos origin) {
        this.origin = origin;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("OriginX", origin.getX());
        tag.putInt("OriginY", origin.getY());
        tag.putInt("OriginZ", origin.getZ());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        origin = new BlockPos(tag.getInt("OriginX"), tag.getInt("OriginY"), tag.getInt("OriginZ"));
    }
}