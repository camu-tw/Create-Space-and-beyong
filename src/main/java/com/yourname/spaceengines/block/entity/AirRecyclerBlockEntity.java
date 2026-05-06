package com.yourname.spaceengines.block.entity;

import com.yourname.spaceengines.SpaceEnginesConfig;
import com.yourname.spaceengines.SpaceEnginesMod;
import com.yourname.spaceengines.data.HullPropertySavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AirRecyclerBlockEntity extends BlockEntity {
    private float recycledAir;

    public AirRecyclerBlockEntity(BlockPos pos, BlockState blockState) {
        super(SpaceEnginesMod.AIR_RECYCLER_BLOCK_ENTITY.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AirRecyclerBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }

        int signal = level.getBestNeighborSignal(pos);
        float rpmEquivalent = signal * 16.0f;
        if (rpmEquivalent <= 0.0f) {
            return;
        }

        float refill = (float) (rpmEquivalent * SpaceEnginesConfig.AIR_RECYCLER_RPM_EFFICIENCY.get() / 100.0d);
        blockEntity.recycledAir += refill;

        if (level instanceof ServerLevel serverLevel) {
            HullPropertySavedData data = HullPropertySavedData.get(serverLevel);
            data.addRecycledAir(Math.max(0.0f, refill));
        }

        blockEntity.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putFloat("RecycledAir", recycledAir);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        recycledAir = tag.getFloat("RecycledAir");
    }
}